package com.yehorlevchenko.data.storage.remote.entity

import com.google.gson.annotations.SerializedName

data class MainWeatherInfoApiEntity(
    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("humidity")
    val humidity: Int
)