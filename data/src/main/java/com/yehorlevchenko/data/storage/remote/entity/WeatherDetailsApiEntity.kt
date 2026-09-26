package com.yehorlevchenko.data.storage.remote.entity

import com.google.gson.annotations.SerializedName

data class WeatherDetailsApiEntity(
    @SerializedName("main")
    val description: String,

    @SerializedName("icon")
    val iconId: String
)