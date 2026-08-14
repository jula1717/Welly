package com.jula1717.welly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.entity.DrinkEntity
import com.jula1717.welly.data.local.entity.MealEntity

@Database(
    entities = [MealEntity::class, DrinkEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class WellyDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    abstract fun drinkDao(): DrinkDao

    companion object {
        const val DATABASE_NAME = "welly.db"
    }
}
