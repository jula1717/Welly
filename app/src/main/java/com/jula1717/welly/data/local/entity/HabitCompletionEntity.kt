package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.jula1717.welly.data.local.entity.HabitCompletionEntity.Companion.COLUMN_EPOCH_DAY
import com.jula1717.welly.data.local.entity.HabitCompletionEntity.Companion.COLUMN_HABIT_ID
import com.jula1717.welly.data.local.entity.HabitEntity.Companion.COLUMN_ID

@Entity(
    tableName = HabitCompletionEntity.TABLE_NAME,
    primaryKeys = [COLUMN_HABIT_ID, COLUMN_EPOCH_DAY],
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = [COLUMN_ID],
            childColumns = [COLUMN_HABIT_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class HabitCompletionEntity(
    val habitId: Long,
    val epochDay: Long,
    val count: Int,
) {
    companion object {
        const val TABLE_NAME = "habit_completions"
        const val COLUMN_HABIT_ID = "habitId"
        const val COLUMN_EPOCH_DAY = "epochDay"
    }
}
