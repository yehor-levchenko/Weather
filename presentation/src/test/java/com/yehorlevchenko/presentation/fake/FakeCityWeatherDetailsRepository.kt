package com.yehorlevchenko.presentation.fake

import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository

class FakeCityWeatherDetailsRepository : CityWeatherDetailsRepository {

    val storedDetails = mutableMapOf<Long, CityWeatherDetails>()
    var actualDetails: CityWeatherDetails? = null
    var error: Throwable? = null

    override suspend fun getStoredCityWeatherDetailsByCityWeatherId(
        cityWeatherId: Long
    ): Result<CityWeatherDetails> {
        error?.let { return Result.failure(it) }
        return Result.success(checkNotNull(storedDetails[cityWeatherId]))
    }

    override suspend fun getActualCityWeatherDetailsByCityName(
        cityName: String,
        cityId: Long
    ): Result<CityWeatherDetails> {
        error?.let { return Result.failure(it) }
        return Result.success(checkNotNull(actualDetails))
    }
}
