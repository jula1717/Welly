package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jula1717.welly.data.local.entity.HabitEntity.Companion.COLUMN_BUILT_IN
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitPeriodUnit

@Entity(
    tableName = HabitEntity.TABLE_NAME,
    indices = [Index(value = [COLUMN_BUILT_IN], unique = true)],
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val builtIn: BuiltInHabit? = null,
    val name: String?,
    val points: Int,
    val times: Int,
    val periodLength: Int,
    val periodUnit: HabitPeriodUnit,
    val isEnabled: Boolean = true,
    val startEpochDay: Long,
) {
    companion object {
        const val TABLE_NAME = "habits"
        const val COLUMN_ID = "id"
        const val COLUMN_BUILT_IN = "builtIn"
    }
}
