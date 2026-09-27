package com.jula1717.welly.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateActivityUseCaseTest {
    private val validate = ValidateActivityUseCase()

    @Test
    fun `plain name is valid`() {
        assertTrue(validate("Yoga"))
    }

    @Test
    fun `trailing whitespace is allowed`() {
        assertTrue(validate("Yoga  "))
    }

    @Test
    fun `leading whitespace is rejected`() {
        assertFalse(validate("  Yoga"))
        assertFalse(validate("\tYoga"))
    }

    @Test
    fun `empty and blank names are rejected`() {
        assertFalse(validate(""))
        assertFalse(validate("   "))
    }
}
