package com.yehorlevchenko.presentation.mapper

import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.presentation.entity.CityScreenEntity

fun City.toCityScreenEntity() = CityScreenEntity(
    id = id,
    name = name
)