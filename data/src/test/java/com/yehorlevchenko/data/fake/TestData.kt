package com.yehorlevchenko.data.fake

import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity
import com.yehorlevchenko.data.storage.remote.entity.MainWeatherInfoApiEntity
import com.yehorlevchenko.data.storage.remote.entity.WeatherDetailsApiEntity
import com.yehorlevchenko.data.storage.remote.entity.WindApiEntity

fun createCityWeatherDetailsApiEntity() = CityWeatherDetailsApiEntity(
    weather = listOf(WeatherDetailsApiEntity(description = "Clouds", iconId = "04d")),
    main = MainWeatherInfoApiEntity(temperature = 285.5, humidity = 72),
    wind = WindApiEntity(windSpeed = 4.1)
)
