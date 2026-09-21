package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jula1717.welly.domain.model.ActivityType

@Entity(tableName = ActivityEntity.TABLE_NAME)
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: ActivityType,
) {
    companion object {
        const val TABLE_NAME = "activities"
        const val COLUMN_ID = "id"
    }
}
