package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitCompletion
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class FakeHabitRepository(
    initial: List<Habit> = emptyList(),
) : HabitRepository {
    private val habits = MutableStateFlow(initial)
    private val completions = MutableStateFlow<List<HabitCompletion>>(emptyList())

    val deletedIds = mutableListOf<Long>()

    override fun getHabits(): Flow<List<Habit>> = habits

    override fun getHabit(id: Long): Flow<Habit?> = habits.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun findHabits(): List<Habit> = habits.value

    override suspend fun findHabit(id: Long): Habit? = habits.value.firstOrNull { it.id == id }

    override suspend fun addHabit(habit: Habit) {
        habits.update { list -> list + habit.copy(id = (list.maxOfOrNull { it.id } ?: 0L) + 1) }
    }

    override suspend fun updateHabit(habit: Habit) {
        habits.update { list -> list.map { if (it.id == habit.id) habit else it } }
    }

    override suspend fun deleteHabit(id: Long) {
        deletedIds += id
        habits.update { list -> list.filterNot { it.id == id } }
    }

    override fun getCompletions(
        habitId: Long,
        from: LocalDate,
        to: LocalDate,
    ): Flow<List<HabitCompletion>> =
        completions.map { list ->
            list.filter { it.habitId == habitId && it.date in from..to }
        }

    override suspend fun setCompletionCount(
        habitId: Long,
        date: LocalDate,
        count: Int,
    ) {
        completions.update { list ->
            val others = list.filterNot { it.habitId == habitId && it.date == date }
            if (count > 0) others + HabitCompletion(habitId, date, count) else others
        }
    }
}
