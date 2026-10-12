package com.jula1717.welly.presentation.habitform

import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FrequencyInputTest {
    private fun frequency(
        times: Int,
        length: Int,
        unit: HabitPeriodUnit,
    ) = HabitFrequency(times = times, periodLength = length, periodUnit = unit)

    @Test
    fun `daily needs no numbers`() {
        assertEquals(HabitFrequency.Daily, FrequencyInput(FrequencyOption.Daily).toFrequency())
    }

    @Test
    fun `each option builds its frequency from its own numbers`() {
        assertEquals(
            frequency(2, 1, HabitPeriodUnit.Day),
            FrequencyInput().withValue(FrequencyField.TimesPerDay, 2).toFrequency(),
        )
        assertEquals(
            frequency(1, 3, HabitPeriodUnit.Day),
            FrequencyInput().withValue(FrequencyField.EveryDays, 3).toFrequency(),
        )
        assertEquals(
            frequency(4, 1, HabitPeriodUnit.Week),
            FrequencyInput().withValue(FrequencyField.TimesPerWeek, 4).toFrequency(),
        )
        assertEquals(
            frequency(5, 1, HabitPeriodUnit.Month),
            FrequencyInput().withValue(FrequencyField.TimesPerMonth, 5).toFrequency(),
        )
        assertEquals(
            frequency(10, 14, HabitPeriodUnit.Day),
            FrequencyInput()
                .withValue(FrequencyField.TimesInDays, 10)
                .withValue(FrequencyField.DaysWindow, 14)
                .toFrequency(),
        )
    }

    @Test
    fun `typing into a field selects the option it belongs to`() {
        val input = FrequencyInput().withValue(FrequencyField.TimesPerMonth, 2)

        assertEquals(FrequencyOption.TimesPerMonth, input.option)
    }

    @Test
    fun `fields of different options never share a value`() {
        val input =
            FrequencyInput()
                .withValue(FrequencyField.TimesPerDay, 5)
                .withValue(FrequencyField.EveryDays, 3)

        assertNull(input.valueOf(FrequencyField.TimesPerWeek))
        assertNull(input.valueOf(FrequencyField.TimesPerMonth))
        assertNull(input.valueOf(FrequencyField.TimesInDays))
        assertNull(input.valueOf(FrequencyField.DaysWindow))
        assertEquals(5, input.valueOf(FrequencyField.TimesPerDay))
        assertEquals(3, input.valueOf(FrequencyField.EveryDays))
    }

    @Test
    fun `switching back to a row finds the numbers that were typed there`() {
        val input =
            FrequencyInput()
                .withValue(FrequencyField.TimesPerDay, 5)
                .withValue(FrequencyField.TimesPerWeek, 2)
                .copy(option = FrequencyOption.TimesPerDay)

        assertEquals(frequency(5, 1, HabitPeriodUnit.Day), input.toFrequency())
    }

    @Test
    fun `clearing a field makes the frequency incomplete`() {
        val filled = FrequencyInput().withValue(FrequencyField.TimesPerWeek, 2)
        val input = filled.withValue(FrequencyField.TimesPerWeek, null)

        assertNull(input.toFrequency())
    }

    @Test
    fun `is incomplete while a needed number is missing`() {
        assertNull(FrequencyInput(FrequencyOption.TimesPerDay).toFrequency())
        assertNull(FrequencyInput(FrequencyOption.EveryNDays).toFrequency())
        assertNull(FrequencyInput().withValue(FrequencyField.TimesInDays, 3).toFrequency())
        assertNull(FrequencyInput().withValue(FrequencyField.DaysWindow, 3).toFrequency())
    }

    @Test
    fun `describes every stored frequency with the matching option`() {
        assertEquals(FrequencyInput(FrequencyOption.Daily), FrequencyInput.from(HabitFrequency.Daily))
        assertEquals(
            FrequencyInput().withValue(FrequencyField.TimesPerDay, 3),
            FrequencyInput.from(frequency(3, 1, HabitPeriodUnit.Day)),
        )
        assertEquals(
            FrequencyInput().withValue(FrequencyField.EveryDays, 4),
            FrequencyInput.from(frequency(1, 4, HabitPeriodUnit.Day)),
        )
        assertEquals(
            FrequencyInput().withValue(FrequencyField.TimesPerWeek, 1),
            FrequencyInput.from(HabitFrequency.Weekly),
        )
        assertEquals(
            FrequencyInput().withValue(FrequencyField.TimesPerMonth, 2),
            FrequencyInput.from(frequency(2, 1, HabitPeriodUnit.Month)),
        )
    }

    @Test
    fun `a input survives a round trip`() {
        val frequencies =
            listOf(
                HabitFrequency.Daily,
                frequency(3, 1, HabitPeriodUnit.Day),
                frequency(1, 5, HabitPeriodUnit.Day),
                frequency(2, 7, HabitPeriodUnit.Day),
                frequency(3, 1, HabitPeriodUnit.Week),
                frequency(4, 1, HabitPeriodUnit.Month),
            )

        frequencies.forEach { assertEquals(it, FrequencyInput.from(it).toFrequency()) }
    }
}
