package com.yehorlevchenko.domain.repository

import com.yehorlevchenko.domain.entity.CityWeatherDetails

interface CityWeatherDetailsRepository {

    suspend fun getStoredCityWeatherDetailsByCityWeatherId(
        cityWeatherId: Long
    ): Result<CityWeatherDetails>

    suspend fun getActualCityWeatherDetailsByCityName(
        cityName: String, cityId: Long
    ): Result<CityWeatherDetails>
}