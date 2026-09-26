package com.yehorlevchenko.data.storage.remote.api

import com.yehorlevchenko.data.storage.remote.entity.CityWeatherDetailsApiEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface CityWeatherDetailsApi {

    @GET("weather")
    suspend fun getActualCityWeatherDetailsByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): CityWeatherDetailsApiEntity
}