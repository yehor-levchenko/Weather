package com.yehorlevchenko.presentation.entity

data class CityWeatherDetailsScreenEntity(
    val cityName: String,
    val description: String,
    val temperature: String,
    val humidity: String,
    val windSpeed: String,
    val date: String,
    val iconUrl: String
)