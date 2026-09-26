package com.yehorlevchenko.data.storage.local.datasource.history

import com.yehorlevchenko.data.storage.local.db.CityWeatherHistoryDao
import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity

class LocalCityWeatherHistoryDataSourceImpl(
    private val cityWeatherHistoryDao: CityWeatherHistoryDao
) : LocalCityWeatherHistoryDataSource {

    override suspend fun getCityWeatherHistoryByCityId(
        cityId: Long
    ): List<CityWeatherHistoryEntity> {
        return cityWeatherHistoryDao.getCityWeatherHistoryByCityId(cityId)
    }
}