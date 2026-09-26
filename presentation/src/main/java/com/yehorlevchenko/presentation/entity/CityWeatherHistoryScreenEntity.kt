package com.yehorlevchenko.presentation.entity

data class CityWeatherHistoryScreenEntity(
    val id: Long,
    val description: String,
    val temperature: String,
    val date: String,
    val iconUrl: String
)