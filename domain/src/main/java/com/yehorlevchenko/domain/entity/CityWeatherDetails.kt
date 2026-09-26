package com.yehorlevchenko.domain.entity

data class CityWeatherDetails(
    val id: Long,
    val cityId: Long,
    val description: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val date: Long,
    val iconId: String
)