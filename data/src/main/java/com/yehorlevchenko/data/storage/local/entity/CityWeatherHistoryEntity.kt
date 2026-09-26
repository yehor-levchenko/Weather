package com.yehorlevchenko.data.storage.local.entity

data class CityWeatherHistoryEntity(
    val id: Long,
    val description: String,
    val temperature: Double,
    val date: Long,
    val iconId: String
)