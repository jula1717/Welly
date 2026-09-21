package com.jula1717.welly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jula1717.welly.data.local.dao.ActivityDao
import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.dao.WorkoutDao
import com.jula1717.welly.data.local.entity.ActivityEntity
import com.jula1717.welly.data.local.entity.DrinkEntity
import com.jula1717.welly.data.local.entity.MealEntity
import com.jula1717.welly.data.local.entity.WorkoutEntity

@Database(
    entities = [MealEntity::class, DrinkEntity::class, ActivityEntity::class, WorkoutEntity::class],
    version = 3,
    exportSchema = true,
)
abstract class WellyDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    abstract fun drinkDao(): DrinkDao

    abstract fun activityDao(): ActivityDao

    abstract fun workoutDao(): WorkoutDao

    companion object {
        const val DATABASE_NAME = "welly.db"
    }
}
