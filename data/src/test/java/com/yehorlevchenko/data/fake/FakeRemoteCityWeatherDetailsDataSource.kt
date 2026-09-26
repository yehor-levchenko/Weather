package com.yehorlevchenko.data.fake

import com.yehorlevchenko.data.storage.remote.datasource.details.RemoteCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity

class FakeRemoteCityWeatherDetailsDataSource : RemoteCityWeatherDetailsDataSource {

    var response: CityWeatherDetailsApiEntity? = null
    var error: Throwable? = null
    val requestedCityNames = mutableListOf<String>()

    override suspend fun getActualCityWeatherDetailsByCityName(
        cityName: String
    ): CityWeatherDetailsApiEntity {
        requestedCityNames += cityName
        error?.let { throw it }
        return checkNotNull(response)
    }
}
