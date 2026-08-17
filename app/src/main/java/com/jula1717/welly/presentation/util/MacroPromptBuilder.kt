package com.jula1717.welly.presentation.util

fun buildMacroPrompt(context: String): String =
    "Calculate macros for: $context\n" +
        "Respond only in JSON:\n" +
        "{\"protein_g\": 0, \"carbs_g\": 0, \"fat_g\": 0, \"fiber_g\": 0, \"kcal\": 0}"
