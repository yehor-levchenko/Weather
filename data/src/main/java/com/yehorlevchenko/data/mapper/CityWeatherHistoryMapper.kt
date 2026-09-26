package com.yehorlevchenko.data.mapper

import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity
import com.yehorlevchenko.domain.entity.CityWeatherHistory

fun CityWeatherHistoryEntity.toCityWeatherHistory() = CityWeatherHistory(
    id = id,
    description = description,
    temperature = temperature,
    date = date,
    iconId = iconId
)