package com.yehorlevchenko.data.storage.remote.datasource.details

import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity

interface RemoteCityWeatherDetailsDataSource {

    suspend fun getActualCityWeatherDetailsByCityName(cityName: String): CityWeatherDetailsApiEntity
}