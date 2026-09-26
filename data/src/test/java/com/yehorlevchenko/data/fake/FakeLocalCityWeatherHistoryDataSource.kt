package com.yehorlevchenko.data.fake

import com.yehorlevchenko.data.storage.local.datasource.history.LocalCityWeatherHistoryDataSource
import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity

class FakeLocalCityWeatherHistoryDataSource : LocalCityWeatherHistoryDataSource {

    val history = mutableMapOf<Long, List<CityWeatherHistoryEntity>>()
    var error: Throwable? = null

    override suspend fun getCityWeatherHistoryByCityId(cityId: Long): List<CityWeatherHistoryEntity> {
        error?.let { throw it }
        return history[cityId].orEmpty()
    }
}
