package com.jula1717.welly.presentation.profile

import org.junit.Assert.assertEquals
import org.junit.Test

class RulerFieldTest {
    private val range = 130..220

    @Test
    fun `no travel keeps the start value`() {
        assertEquals(170, rulerStepTarget(startValue = 170, accumulatedPx = 0f, stepPx = 5f, range = range))
    }

    @Test
    fun `dragging right counts down, dragging left counts up`() {
        assertEquals(165, rulerStepTarget(startValue = 170, accumulatedPx = 25f, stepPx = 5f, range = range))
        assertEquals(175, rulerStepTarget(startValue = 170, accumulatedPx = -25f, stepPx = 5f, range = range))
    }

    @Test
    fun `travel shorter than a full step is truncated, not rounded`() {
        assertEquals(168, rulerStepTarget(startValue = 170, accumulatedPx = 14f, stepPx = 5f, range = range))
        assertEquals(172, rulerStepTarget(startValue = 170, accumulatedPx = -14f, stepPx = 5f, range = range))
    }

    @Test
    fun `result is clamped to the range at both ends`() {
        assertEquals(220, rulerStepTarget(startValue = 210, accumulatedPx = -100f, stepPx = 5f, range = range))
        assertEquals(130, rulerStepTarget(startValue = 140, accumulatedPx = 100f, stepPx = 5f, range = range))
    }
}
