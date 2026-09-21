package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jula1717.welly.data.local.entity.ActivityEntity.Companion.COLUMN_ID
import com.jula1717.welly.data.local.entity.WorkoutEntity.Companion.COLUMN_ACTIVITY_ID

@Entity(
    tableName = WorkoutEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = [COLUMN_ID],
            childColumns = [COLUMN_ACTIVITY_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(COLUMN_ACTIVITY_ID)],
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val activityId: Long,
    val dateTimeEpochSecond: Long,
) {
    companion object {
        const val TABLE_NAME = "workouts"
        const val COLUMN_ACTIVITY_ID = "activityId"
    }
}
