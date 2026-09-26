package com.yehorlevchenko.data.storage.remote.datasource.details

import com.yehorlevchenko.data.BuildConfig
import com.yehorlevchenko.data.storage.remote.api.CityWeatherDetailsApi
import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity

class RemoteCityWeatherDetailsDataSourceImpl(
    private val api: CityWeatherDetailsApi
) : RemoteCityWeatherDetailsDataSource {

    override suspend fun getActualCityWeatherDetailsByCityName(
        cityName: String
    ): CityWeatherDetailsApiEntity {
        return api.getActualCityWeatherDetailsByCityName(cityName, BuildConfig.API_KEY)
    }
}