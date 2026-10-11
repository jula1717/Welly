package com.jula1717.welly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jula1717.welly.data.local.dao.ActivityDao
import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.dao.HabitCompletionDao
import com.jula1717.welly.data.local.dao.HabitDao
import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.dao.WorkoutDao
import com.jula1717.welly.data.local.entity.ActivityEntity
import com.jula1717.welly.data.local.entity.DrinkEntity
import com.jula1717.welly.data.local.entity.HabitCompletionEntity
import com.jula1717.welly.data.local.entity.HabitEntity
import com.jula1717.welly.data.local.entity.MealEntity
import com.jula1717.welly.data.local.entity.WorkoutEntity

@Database(
    entities = [
        MealEntity::class,
        DrinkEntity::class,
        ActivityEntity::class,
        WorkoutEntity::class,
        HabitEntity::class,
        HabitCompletionEntity::class,
    ],
    version = 5,
    exportSchema = true,
)
abstract class WellyDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    abstract fun drinkDao(): DrinkDao

    abstract fun activityDao(): ActivityDao

    abstract fun workoutDao(): WorkoutDao

    abstract fun habitDao(): HabitDao

    abstract fun habitCompletionDao(): HabitCompletionDao

    companion object {
        const val DATABASE_NAME = "welly.db"
    }
}
