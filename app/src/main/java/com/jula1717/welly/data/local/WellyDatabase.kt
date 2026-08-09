package com.jula1717.welly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.entity.MealEntity

@Database(
    entities = [MealEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class WellyDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        const val DATABASE_NAME = "welly.db"
    }
}
