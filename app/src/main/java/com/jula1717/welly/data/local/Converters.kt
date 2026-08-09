package com.jula1717.welly.data.local

import androidx.room.TypeConverter
import com.jula1717.welly.domain.model.MealType

class Converters {
    @TypeConverter
    fun fromMealType(type: MealType): String = type.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)
}
