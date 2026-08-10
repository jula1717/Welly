package com.jula1717.welly.presentation.components

import androidx.annotation.StringRes
import com.jula1717.welly.R

enum class MacroType {
    CALORIES,
    PROTEIN,
    CARBS,
    FAT,
    FIBER
}

@get:StringRes
internal val MacroType.contentDescriptionResId: Int
    get() = when (this) {
        MacroType.CALORIES -> R.string.cd_macro_calories
        MacroType.PROTEIN -> R.string.cd_macro_protein
        MacroType.CARBS -> R.string.cd_macro_carbs
        MacroType.FAT -> R.string.cd_macro_fat
        MacroType.FIBER -> R.string.cd_macro_fiber
    }

@get:StringRes
internal val MacroType.titleResId: Int
    get() = when (this) {
        MacroType.CALORIES -> R.string.macro_calories
        MacroType.PROTEIN -> R.string.macro_protein
        MacroType.CARBS -> R.string.macro_carbs
        MacroType.FAT -> R.string.macro_fat
        MacroType.FIBER -> R.string.macro_fiber
    }
