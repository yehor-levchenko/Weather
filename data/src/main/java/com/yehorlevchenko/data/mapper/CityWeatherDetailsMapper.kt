package com.yehorlevchenko.data.mapper

import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity
import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity
import com.yehorlevchenko.domain.entity.CityWeatherDetails

fun CityWeatherDetails.toCityWeatherDetailsDbEntity(cityId: Long) = CityWeatherDetailsDbEntity(
    id = id,
    cityId = cityId,
    description = description,
    temperature = temperature,
    humidity = humidity,
    windSpeed = windSpeed,
    date = date,
    iconId = iconId
)

fun CityWeatherDetailsDbEntity.toCityWeatherDetails() = CityWeatherDetails(
    id = id,
    cityId = cityId,
    description = description,
    temperature = temperature,
    humidity = humidity,
    windSpeed = windSpeed,
    date = date,
    iconId = iconId
)

fun CityWeatherDetailsApiEntity.toCityWeatherDetails(date: Long) = CityWeatherDetails(
    id = 0,
    cityId = 0,
    description = weather[0].description,
    temperature = main.temperature,
    humidity = main.humidity,
    windSpeed = wind.windSpeed,
    date = date,
    iconId = weather[0].iconId
)