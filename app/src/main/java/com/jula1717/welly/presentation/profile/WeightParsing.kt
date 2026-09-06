package com.jula1717.welly.presentation.profile

import kotlin.math.roundToInt

private const val WEIGHT_MAX_INT_DIGITS = 3
private const val WEIGHT_MAX_FRACTION_DIGITS = 1
private const val TENTHS_PER_UNIT = 10

/** Keeps digits and a single `.`/`,`; caps the integer and fractional lengths. */
internal fun sanitizeWeightInput(raw: String): String {
    val normalized = raw.replace(',', '.')
    val firstDot = normalized.indexOf('.')
    val digitsAndDot =
        normalized.filterIndexed { index, c -> c.isDigit() || (c == '.' && index == firstDot) }
    val dot = digitsAndDot.indexOf('.')
    if (dot < 0) return digitsAndDot.take(WEIGHT_MAX_INT_DIGITS)
    val intPart = digitsAndDot.substring(0, dot).take(WEIGHT_MAX_INT_DIGITS)
    val fractionPart = digitsAndDot.substring(dot + 1).take(WEIGHT_MAX_FRACTION_DIGITS)
    return "$intPart.$fractionPart"
}

internal fun parseWeight(text: String): Double? {
    val value = text.toDoubleOrNull() ?: return null
    return (value * TENTHS_PER_UNIT).roundToInt() / TENTHS_PER_UNIT.toDouble()
}

internal fun formatWeight(kg: Double): String {
    val tenths = (kg * TENTHS_PER_UNIT).roundToInt()
    return "${tenths / TENTHS_PER_UNIT}.${tenths % TENTHS_PER_UNIT}"
}
