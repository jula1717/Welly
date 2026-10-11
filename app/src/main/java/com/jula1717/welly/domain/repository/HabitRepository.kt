package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitCompletion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun getHabits(): Flow<List<Habit>>

    fun getHabit(id: Long): Flow<Habit?>

    suspend fun findHabits(): List<Habit>

    suspend fun findHabit(id: Long): Habit?

    suspend fun addHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun deleteHabit(id: Long)

    fun getCompletions(
        habitId: Long,
        from: LocalDate,
        to: LocalDate,
    ): Flow<List<HabitCompletion>>

    suspend fun setCompletionCount(
        habitId: Long,
        date: LocalDate,
        count: Int,
    )
}
