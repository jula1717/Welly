package com.jula1717.welly.di

import android.content.Context
import androidx.room.Room
import com.jula1717.welly.data.local.WellyDatabase
import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.repository.DrinkRepositoryImpl
import com.jula1717.welly.data.repository.MealRepositoryImpl
import com.jula1717.welly.domain.repository.DrinkRepository
import com.jula1717.welly.domain.repository.MealRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideWellyDatabase(
        @ApplicationContext context: Context,
    ): WellyDatabase =
        Room
            .databaseBuilder(
                context,
                WellyDatabase::class.java,
                WellyDatabase.DATABASE_NAME,
            )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideMealDao(database: WellyDatabase): MealDao = database.mealDao()

    @Provides
    fun provideDrinkDao(database: WellyDatabase): DrinkDao = database.drinkDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

    @Binds
    @Singleton
    abstract fun bindDrinkRepository(impl: DrinkRepositoryImpl): DrinkRepository
}
