package com.jula1717.welly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DrinkEntity.TABLE_NAME)
data class DrinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateTimeEpochSecond: Long,
    val amountMl: Int,
    val description: String,
    val calories: Int?,
    val protein: Int?,
    val carbs: Int?,
    val fat: Int?,
    val fiber: Int?,
) {
    companion object {
        const val TABLE_NAME = "drinks"
    }
}
