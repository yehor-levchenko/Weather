package com.yehorlevchenko.presentation.mapper

import com.yehorlevchenko.domain.entity.CityWeatherHistory

import com.yehorlevchenko.presentation.entity.CityWeatherHistoryScreenEntity

fun CityWeatherHistory.toCityWeatherHistoryScreenEntity(
    temperature: Double,
    date: String,
    iconUrl: String
) = CityWeatherHistoryScreenEntity(
    id = id,
    description = description,
    temperature = temperature.toString(),
    date = date,
    iconUrl = iconUrl
)