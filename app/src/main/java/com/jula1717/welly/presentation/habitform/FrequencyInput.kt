package com.jula1717.welly.presentation.habitform

import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit

enum class FrequencyOption {
    Daily,
    TimesPerDay,
    EveryNDays,
    TimesPerWeek,
    TimesPerMonth,
    TimesPerNDays,
}

enum class FrequencyField(
    val option: FrequencyOption,
) {
    TimesPerDay(FrequencyOption.TimesPerDay),
    EveryDays(FrequencyOption.EveryNDays),
    TimesPerWeek(FrequencyOption.TimesPerWeek),
    TimesPerMonth(FrequencyOption.TimesPerMonth),
    TimesInDays(FrequencyOption.TimesPerNDays),
    DaysWindow(FrequencyOption.TimesPerNDays),
}

data class FrequencyInput(
    val option: FrequencyOption = FrequencyOption.Daily,
    val values: Map<FrequencyField, Int> = emptyMap(),
) {
    fun valueOf(field: FrequencyField): Int? = values[field]

    fun withValue(
        field: FrequencyField,
        value: Int?,
    ): FrequencyInput =
        copy(
            option = field.option,
            values = if (value == null) values - field else values + (field to value),
        )

    fun toFrequency(): HabitFrequency? =
        when (option) {
            FrequencyOption.Daily -> HabitFrequency.Daily
            FrequencyOption.TimesPerDay -> times(FrequencyField.TimesPerDay, 1, HabitPeriodUnit.Day)
            FrequencyOption.EveryNDays ->
                valueOf(FrequencyField.EveryDays)?.let { HabitFrequency(1, it, HabitPeriodUnit.Day) }
            FrequencyOption.TimesPerWeek -> times(FrequencyField.TimesPerWeek, 1, HabitPeriodUnit.Week)
            FrequencyOption.TimesPerMonth -> times(FrequencyField.TimesPerMonth, 1, HabitPeriodUnit.Month)
            FrequencyOption.TimesPerNDays -> {
                val times = valueOf(FrequencyField.TimesInDays)
                val days = valueOf(FrequencyField.DaysWindow)
                if (times != null && days != null) HabitFrequency(times, days, HabitPeriodUnit.Day) else null
            }
        }

    private fun times(
        field: FrequencyField,
        length: Int,
        unit: HabitPeriodUnit,
    ): HabitFrequency? = valueOf(field)?.let { HabitFrequency(it, length, unit) }

    companion object {
        fun from(frequency: HabitFrequency): FrequencyInput {
            val times = frequency.times
            val length = frequency.periodLength
            return when (frequency.periodUnit) {
                HabitPeriodUnit.Day -> fromDays(times, length)
                HabitPeriodUnit.Week -> FrequencyInput().withValue(FrequencyField.TimesPerWeek, times)
                HabitPeriodUnit.Month -> FrequencyInput().withValue(FrequencyField.TimesPerMonth, times)
            }
        }

        private fun fromDays(
            times: Int,
            days: Int,
        ): FrequencyInput =
            when {
                days == 1 && times == 1 -> FrequencyInput(FrequencyOption.Daily)
                days == 1 -> FrequencyInput().withValue(FrequencyField.TimesPerDay, times)
                times == 1 -> FrequencyInput().withValue(FrequencyField.EveryDays, days)
                else ->
                    FrequencyInput()
                        .withValue(FrequencyField.TimesInDays, times)
                        .withValue(FrequencyField.DaysWindow, days)
            }
    }
}
