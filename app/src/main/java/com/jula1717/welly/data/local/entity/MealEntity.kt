package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jula1717.welly.domain.model.MealType

@Entity(tableName = MealEntity.TABLE_NAME)
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateTimeEpochSecond: Long,
    val type: MealType,
    val description: String,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val fiber: Int,
    val calories: Int,
) {
    companion object {
        const val TABLE_NAME = "meals"
    }
}
