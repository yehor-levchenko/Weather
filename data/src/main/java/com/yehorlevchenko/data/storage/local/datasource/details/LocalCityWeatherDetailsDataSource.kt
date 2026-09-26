package com.yehorlevchenko.data.storage.local.datasource.details

import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity

interface LocalCityWeatherDetailsDataSource {

    suspend fun getStoredCityWeatherDetailsByCityWeatherId(cityWeatherId: Long): CityWeatherDetailsDbEntity?

    suspend fun saveCityWeatherDetails(cityWeatherDetails: CityWeatherDetailsDbEntity)
}