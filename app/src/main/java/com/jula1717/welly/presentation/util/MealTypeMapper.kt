package com.jula1717.welly.presentation.util

import androidx.annotation.StringRes
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.MealType

@get:StringRes
internal val MealType.titleResId: Int
    get() = when (this) {
        MealType.Breakfast -> R.string.meal_type_breakfast
        MealType.Lunch -> R.string.meal_type_lunch
        MealType.Dinner -> R.string.meal_type_dinner
        MealType.Snack -> R.string.meal_type_snack
    }
