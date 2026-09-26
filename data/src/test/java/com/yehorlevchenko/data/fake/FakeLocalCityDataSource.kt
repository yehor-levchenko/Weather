package com.yehorlevchenko.data.fake

import com.yehorlevchenko.data.storage.local.datasource.city.LocalCityDataSource
import com.yehorlevchenko.data.storage.local.entity.CityDbEntity

class FakeLocalCityDataSource : LocalCityDataSource {

    val cities = mutableListOf<CityDbEntity>()
    var error: Throwable? = null

    override suspend fun saveCity(city: CityDbEntity) {
        error?.let { throw it }
        cities += city.copy(id = (cities.maxOfOrNull { it.id } ?: 0) + 1)
    }

    override suspend fun deleteCity(cityId: Long) {
        error?.let { throw it }
        cities.removeAll { it.id == cityId }
    }

    override suspend fun getAllCities(): List<CityDbEntity> {
        error?.let { throw it }
        return cities.toList()
    }
}
