package com.yehorlevchenko.data.mapper

import com.yehorlevchenko.data.storage.local.entity.CityDbEntity
import com.yehorlevchenko.domain.entity.City

fun City.toCityDbEntity() = CityDbEntity(
    id = id,
    name = name
)

fun CityDbEntity.toCity() = City(
    id = id,
    name = name
)