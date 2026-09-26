package com.yehorlevchenko.domain.entity

data class CityWeatherHistory(
    val id: Long,
    val description: String,
    val temperature: Double,
    val date: Long,
    val iconId: String
)