package com.yehorlevchenko.data.storage.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

const val TABLE_NAME_CITY_WEATHER_DETAILS = "city_weather_details"

@Entity(
    tableName = TABLE_NAME_CITY_WEATHER_DETAILS,
    foreignKeys = [
        ForeignKey(
            entity = CityDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("cityId")]
)
data class CityWeatherDetailsDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityId: Long,
    val description: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val date: Long,
    val iconId: String
)