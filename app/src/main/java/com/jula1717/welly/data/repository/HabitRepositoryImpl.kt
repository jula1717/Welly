package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.HabitCompletionDao
import com.jula1717.welly.data.local.dao.HabitDao
import com.jula1717.welly.data.local.entity.HabitCompletionEntity
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitCompletion
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl
    @Inject
    constructor(
        private val habitDao: HabitDao,
        private val completionDao: HabitCompletionDao,
    ) : HabitRepository {
        override fun getHabits(): Flow<List<Habit>> =
            habitDao.getHabits().map { entities -> entities.map { it.toDomain() } }

        override fun getHabit(id: Long): Flow<Habit?> = habitDao.getHabit(id).map { it?.toDomain() }

        override suspend fun findHabits(): List<Habit> = habitDao.findHabits().map { it.toDomain() }

        override suspend fun findHabit(id: Long): Habit? = habitDao.findHabit(id)?.toDomain()

        override suspend fun addHabit(habit: Habit) {
            habitDao.insert(habit.toEntity())
        }

        override suspend fun updateHabit(habit: Habit) {
            habitDao.update(habit.toEntity())
        }

        override suspend fun deleteHabit(id: Long) {
            habitDao.delete(id)
        }

        override fun getCompletions(
            habitId: Long,
            from: LocalDate,
            to: LocalDate,
        ): Flow<List<HabitCompletion>> =
            completionDao
                .getCompletions(habitId, from.toEpochDay(), to.toEpochDay())
                .map { entities -> entities.map { it.toDomain() } }

        override suspend fun setCompletionCount(
            habitId: Long,
            date: LocalDate,
            count: Int,
        ) {
            if (count > 0) {
                completionDao.upsert(HabitCompletionEntity(habitId, date.toEpochDay(), count))
            } else {
                completionDao.delete(habitId, date.toEpochDay())
            }
        }
    }
