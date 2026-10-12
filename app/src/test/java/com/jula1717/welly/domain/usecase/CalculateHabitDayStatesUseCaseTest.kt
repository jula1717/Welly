package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.LocalDate

class CalculateHabitDayStatesUseCaseTest {
    private val useCase = CalculateHabitDayStatesUseCase(GetHabitPeriodUseCase())

    private val tuesday = HABIT_MONDAY.plusDays(1)
    private val wednesday = HABIT_MONDAY.plusDays(2)
    private val sunday = HABIT_MONDAY.plusDays(6)
    private val twicePerWeek = testHabit(frequency = HabitFrequency(2, 1, HabitPeriodUnit.Week))

    private fun statuses(
        habit: Habit,
        logs: Map<LocalDate, HabitDayLog>,
    ) = useCase(habit, logs, HABIT_MONDAY..sunday, HABIT_TODAY).mapValues { it.value.status }

    @Test
    fun `weekly quota met keeps the remaining days of the week covered`() {
        val result = statuses(twicePerWeek, logsOn(HABIT_MONDAY, tuesday))

        assertEquals(
            mapOf(
                HABIT_MONDAY to HabitDayStatus.Done,
                tuesday to HabitDayStatus.Done,
                wednesday to HabitDayStatus.Covered,
                HABIT_TODAY to HabitDayStatus.Covered,
            ),
            result,
        )
    }

    @Test
    fun `days before the quota was reached are not covered`() {
        val result = statuses(twicePerWeek, logsOn(wednesday, HABIT_TODAY))

        assertEquals(HabitDayStatus.None, result[HABIT_MONDAY])
        assertEquals(HabitDayStatus.None, result[tuesday])
        assertEquals(HabitDayStatus.Done, result[wednesday])
        assertEquals(HabitDayStatus.Done, result[HABIT_TODAY])
    }

    @Test
    fun `unmet weekly quota leaves days without color unless done`() {
        val result = statuses(twicePerWeek, logsOn(HABIT_MONDAY))

        assertEquals(HabitDayStatus.Done, result[HABIT_MONDAY])
        assertEquals(HabitDayStatus.None, result[tuesday])
        assertEquals(HabitDayStatus.None, result[HABIT_TODAY])
    }

    @Test
    fun `daily habit is done only on days with a log`() {
        val result = statuses(testHabit(), logsOn(HABIT_MONDAY))

        assertEquals(HabitDayStatus.Done, result[HABIT_MONDAY])
        assertEquals(HabitDayStatus.None, result[tuesday])
    }

    @Test
    fun `daily habit needing several times is not done until the count is reached`() {
        val habit = testHabit(frequency = HabitFrequency(2, 1, HabitPeriodUnit.Day))
        val logs = mapOf(HABIT_MONDAY to HabitDayLog(count = 1), tuesday to HabitDayLog(count = 2))

        val result = useCase(habit, logs, HABIT_MONDAY..tuesday, HABIT_TODAY)

        assertEquals(HabitDayStatus.None, result.getValue(HABIT_MONDAY).status)
        assertEquals(1, result.getValue(HABIT_MONDAY).count)
        assertEquals(HabitDayStatus.Done, result.getValue(tuesday).status)
    }

    @Test
    fun `an every-N-days habit covers the days the last N days hold the completion`() {
        val everyFiveDays = testHabit(frequency = HabitFrequency(1, 5, HabitPeriodUnit.Day))
        val saturday = HABIT_MONDAY.plusDays(5)

        val result =
            useCase(everyFiveDays, logsOn(HABIT_MONDAY), HABIT_MONDAY..saturday, saturday)
                .mapValues { it.value.status }

        assertEquals(HabitDayStatus.Done, result[HABIT_MONDAY])
        (1L..4L).forEach { assertEquals(HabitDayStatus.Covered, result[HABIT_MONDAY.plusDays(it)]) }
        assertEquals(HabitDayStatus.None, result[saturday])
    }

    @Test
    fun `an X-times-in-N-days habit is covered only once the quota fits the window`() {
        val twiceInFourDays = testHabit(frequency = HabitFrequency(2, 4, HabitPeriodUnit.Day))
        val logs = logsOn(HABIT_MONDAY)

        val oneDone = useCase(twiceInFourDays, logs, HABIT_MONDAY..tuesday, HABIT_TODAY)
        val twoDone = useCase(twiceInFourDays, logs + logsOn(tuesday), HABIT_MONDAY..wednesday, HABIT_TODAY)

        assertEquals(HabitDayStatus.None, oneDone.getValue(tuesday).status)
        assertEquals(HabitDayStatus.Covered, twoDone.getValue(wednesday).status)
    }

    @Test
    fun `a week does not reset on monday`() {
        val sunday = HABIT_MONDAY.minusDays(1)

        val result = useCase(twicePerWeek, logsOn(sunday, HABIT_MONDAY), sunday..HABIT_TODAY, HABIT_TODAY)

        assertEquals(HabitDayStatus.Covered, result.getValue(HABIT_TODAY).status)
        assertEquals(HabitDayStatus.Covered, result.getValue(wednesday).status)
    }

    @Test
    fun `a month is the last thirty days, not the calendar month`() {
        val fourPerMonth = testHabit(frequency = HabitFrequency(4, 1, HabitPeriodUnit.Month))
        val logs = logsOn(HABIT_TODAY.minusDays(25), HABIT_TODAY.minusDays(20), HABIT_TODAY.minusDays(10), tuesday)

        val result = useCase(fourPerMonth, logs, HABIT_TODAY..HABIT_TODAY, HABIT_TODAY)

        assertEquals(HabitDayStatus.Covered, result.getValue(HABIT_TODAY).status)
    }

    @Test
    fun `a completion older than the window no longer counts`() {
        val twoPerWeek = testHabit(frequency = HabitFrequency(2, 1, HabitPeriodUnit.Week))
        val logs = logsOn(HABIT_TODAY.minusDays(7), HABIT_TODAY.minusDays(1))

        val result = useCase(twoPerWeek, logs, HABIT_TODAY..HABIT_TODAY, HABIT_TODAY)

        assertEquals(HabitDayStatus.None, result.getValue(HABIT_TODAY).status)
    }

    @Test
    fun `future days are omitted`() {
        val result = statuses(testHabit(), emptyMap())

        assertFalse(result.containsKey(HABIT_TODAY.plusDays(1)))
        assertEquals(4, result.size)
    }

    @Test
    fun `activity types are carried to the day state`() {
        val detail = HabitDayDetail.Workouts(setOf(ActivityType.Cardio))
        val logs = mapOf(HABIT_MONDAY to HabitDayLog(count = 2, detail = detail))

        val state = useCase(testHabit(), logs, HABIT_MONDAY..HABIT_MONDAY, HABIT_TODAY).getValue(HABIT_MONDAY)

        assertEquals(detail, state.detail)
    }
}
