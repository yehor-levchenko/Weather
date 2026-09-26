package com.yehorlevchenko.data.storage.remote.entity

import com.google.gson.annotations.SerializedName

data class WindApiEntity(
    @SerializedName("speed")
    val windSpeed: Double
)