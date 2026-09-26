package com.yehorlevchenko.data.storage.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_CITY = "city"

@Entity(tableName = TABLE_NAME_CITY)
data class CityDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)