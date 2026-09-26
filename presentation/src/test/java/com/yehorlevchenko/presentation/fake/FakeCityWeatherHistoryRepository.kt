package com.yehorlevchenko.presentation.fake

import com.yehorlevchenko.domain.entity.CityWeatherHistory
import com.yehorlevchenko.domain.repository.CityWeatherHistoryRepository

class FakeCityWeatherHistoryRepository : CityWeatherHistoryRepository {

    val history = mutableMapOf<Long, List<CityWeatherHistory>>()
    var error: Throwable? = null

    override suspend fun getCityWeatherHistoryByCityId(cityId: Long): Result<List<CityWeatherHistory>> {
        error?.let { return Result.failure(it) }
        return Result.success(history[cityId].orEmpty())
    }
}
