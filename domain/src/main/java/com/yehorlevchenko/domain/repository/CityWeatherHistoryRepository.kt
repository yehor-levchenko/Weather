package com.yehorlevchenko.domain.repository

import com.yehorlevchenko.domain.entity.CityWeatherHistory

interface CityWeatherHistoryRepository {

    suspend fun getCityWeatherHistoryByCityId(
        cityId: Long
    ): Result<List<CityWeatherHistory>>
}