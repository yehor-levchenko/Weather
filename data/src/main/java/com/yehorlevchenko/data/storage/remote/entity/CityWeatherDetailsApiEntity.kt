package com.yehorlevchenko.data.storage.remote.entity

import com.google.gson.annotations.SerializedName

data class CityWeatherDetailsApiEntity(
    @SerializedName("weather")
    val weather: List<WeatherDetailsApiEntity>,

    @SerializedName("main")
    val main: MainWeatherInfoApiEntity,

    @SerializedName("wind")
    val wind: WindApiEntity
)