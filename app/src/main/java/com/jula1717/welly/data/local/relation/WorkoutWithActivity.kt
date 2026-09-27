package com.jula1717.welly.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.jula1717.welly.data.local.entity.ActivityEntity
import com.jula1717.welly.data.local.entity.ActivityEntity.Companion.COLUMN_ID
import com.jula1717.welly.data.local.entity.WorkoutEntity
import com.jula1717.welly.data.local.entity.WorkoutEntity.Companion.COLUMN_ACTIVITY_ID

data class WorkoutWithActivity(
    @Embedded val workout: WorkoutEntity,
    @Relation(parentColumn = COLUMN_ACTIVITY_ID, entityColumn = COLUMN_ID)
    val activity: ActivityEntity,
)
