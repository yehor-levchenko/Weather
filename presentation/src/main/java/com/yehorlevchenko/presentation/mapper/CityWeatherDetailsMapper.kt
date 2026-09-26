package com.yehorlevchenko.presentation.mapper

import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.presentation.entity.CityWeatherDetailsScreenEntity

fun CityWeatherDetails.toCityWeatherDetailsScreenEntity(
    cityName: String,
    temperature: Double,
    windSpeed: Double,
    date: String,
    iconUrl: String
) = CityWeatherDetailsScreenEntity(
    cityName = cityName,
    description = description,
    temperature = temperature.toString(),
    humidity = humidity.toString(),
    windSpeed = windSpeed.toString(),
    date = date,
    iconUrl = iconUrl
)