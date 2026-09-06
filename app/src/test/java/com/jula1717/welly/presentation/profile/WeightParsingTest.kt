package com.jula1717.welly.presentation.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WeightParsingTest {
    // sanitizeWeightInput

    @Test
    fun `strips anything that is not a digit or separator`() {
        assertEquals("70", sanitizeWeightInput("7a0 kg!"))
        assertEquals("70.3", sanitizeWeightInput("  70.3  "))
    }

    @Test
    fun `keeps only the first decimal separator and normalises comma to dot`() {
        assertEquals("70.3", sanitizeWeightInput("70,3"))
        assertEquals("70.3", sanitizeWeightInput("70.3.5"))
        assertEquals("7.0", sanitizeWeightInput("7,0,3"))
    }

    @Test
    fun `caps at one decimal place`() {
        assertEquals("70.3", sanitizeWeightInput("70.35"))
        assertEquals("70.9", sanitizeWeightInput("70.987"))
    }

    @Test
    fun `caps the integer part at three digits`() {
        assertEquals("700", sanitizeWeightInput("7000"))
        assertEquals("180.5", sanitizeWeightInput("1805.5"))
    }

    @Test
    fun `passes a trailing dot through while the user is mid-entry`() {
        assertEquals("70.", sanitizeWeightInput("70."))
    }

    // parseWeight

    @Test
    fun `parses a one-decimal value and clears floating-point drift`() {
        assertEquals(70.1, parseWeight("70.1")!!, 0.0)
        assertEquals(70.0, parseWeight("70")!!, 0.0)
        assertEquals(70.0, parseWeight("70.")!!, 0.0)
    }

    @Test
    fun `is null for blank or non-numeric text`() {
        assertNull(parseWeight(""))
        assertNull(parseWeight("."))
        assertNull(parseWeight("  "))
    }

    @Test
    fun `returns an out-of-range number as-is rather than clamping it`() {
        assertEquals(546.0, parseWeight("546")!!, 0.0)
        assertEquals(5.0, parseWeight("5")!!, 0.0)
    }

    // formatWeight

    @Test
    fun `always renders exactly one decimal`() {
        assertEquals("70.0", formatWeight(70.0))
        assertEquals("70.3", formatWeight(70.3))
        assertEquals("70.1", formatWeight(70.1))
        assertEquals("107.5", formatWeight(107.5))
    }

    @Test
    fun `round-trips through parse and format`() {
        val samples = listOf("35.0", "70.3", "180.0", "42.7")
        samples.forEach { assertEquals(it, formatWeight(parseWeight(it)!!)) }
    }
}
