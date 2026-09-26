package com.yehorlevchenko.data.storage.local.datasource.history

import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity

interface LocalCityWeatherHistoryDataSource {

    suspend fun getCityWeatherHistoryByCityId(cityId: Long): List<CityWeatherHistoryEntity>
}