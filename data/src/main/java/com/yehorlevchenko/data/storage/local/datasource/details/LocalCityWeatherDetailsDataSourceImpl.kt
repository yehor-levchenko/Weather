package com.yehorlevchenko.data.storage.local.datasource.details

import com.yehorlevchenko.data.storage.local.db.CityWeatherDetailsDao
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity

class LocalCityWeatherDetailsDataSourceImpl(
    private val cityWeatherDetailsDao: CityWeatherDetailsDao
) : LocalCityWeatherDetailsDataSource {

    override suspend fun getStoredCityWeatherDetailsByCityWeatherId(
        cityWeatherId: Long
    ): CityWeatherDetailsDbEntity? {
        return cityWeatherDetailsDao.getStoredCityWeatherDetailsByCityWeatherId(cityWeatherId)
    }

    override suspend fun saveCityWeatherDetails(
        cityWeatherDetails: CityWeatherDetailsDbEntity
    ) {
        cityWeatherDetailsDao.saveCityWeatherDetails(cityWeatherDetails)
    }
}