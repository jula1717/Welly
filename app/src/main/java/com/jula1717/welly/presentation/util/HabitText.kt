package com.jula1717.welly.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit

/** The user's own name for the habit, or the localized default for a built-in one. */
@Composable
internal fun Habit.displayName(): String = name ?: builtIn?.let { stringResource(it.nameResId) }.orEmpty()

/** Whether each period is exactly one day, so a habit can be done and counted within a single day. */
internal val HabitFrequency.isSingleDay: Boolean
    get() = periodUnit == HabitPeriodUnit.Day && periodLength == 1

/** A habit that has to be done several times within one day and therefore shows a counter instead of a check. */
internal val HabitFrequency.hasDailyCounter: Boolean
    get() = isSingleDay && times > 1

@Composable
internal fun HabitFrequency.label(): String =
    when {
        this == HabitFrequency.Daily -> stringResource(R.string.habit_frequency_daily)
        isSingleDay -> pluralStringResource(R.plurals.habit_frequency_times_per_day, times, times)
        periodUnit == HabitPeriodUnit.Day && times == 1 ->
            pluralStringResource(R.plurals.habit_frequency_every_n_days, periodLength, periodLength)
        periodUnit == HabitPeriodUnit.Day ->
            pluralStringResource(R.plurals.habit_frequency_times_per_n_days, times, times, periodLength)
        periodUnit == HabitPeriodUnit.Week ->
            pluralStringResource(R.plurals.habit_frequency_times_per_week, times, times)
        else -> pluralStringResource(R.plurals.habit_frequency_times_per_month, times, times)
    }

/** The unit a streak of [count] is measured in: days, since a habit is judged day by day against its window. */
@Composable
internal fun streakUnit(count: Int): String = pluralStringResource(R.plurals.habit_unit_days, count)

/** "34 days in a row", or a note that there is no streak. */
@Composable
internal fun streakText(count: Int): String =
    if (count <= 0) {
        stringResource(R.string.habit_no_streak)
    } else {
        stringResource(R.string.habit_streak_in_a_row, count, streakUnit(count))
    }

/** Progress towards the quota within the last days, or null for habits that repeat within a single day. */
@Composable
internal fun HabitFrequency.periodProgressText(count: Int): String? =
    if (isRolling) {
        pluralStringResource(R.plurals.habit_progress_last_days, windowDays, count, times, windowDays)
    } else {
        null
    }
