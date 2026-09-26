package com.yehorlevchenko.data.fake

import com.yehorlevchenko.data.storage.local.datasource.details.LocalCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity

class FakeLocalCityWeatherDetailsDataSource : LocalCityWeatherDetailsDataSource {

    val details = mutableListOf<CityWeatherDetailsDbEntity>()
    var error: Throwable? = null

    override suspend fun getStoredCityWeatherDetailsByCityWeatherId(
        cityWeatherId: Long
    ): CityWeatherDetailsDbEntity? {
        error?.let { throw it }
        return details.firstOrNull { it.id == cityWeatherId }
    }

    override suspend fun saveCityWeatherDetails(cityWeatherDetails: CityWeatherDetailsDbEntity) {
        error?.let { throw it }
        details += cityWeatherDetails
    }
}
