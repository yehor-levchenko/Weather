package com.yehorlevchenko.data.storage.local.datasource.city

import com.yehorlevchenko.data.storage.local.entity.CityDbEntity

interface LocalCityDataSource {

    suspend fun saveCity(city: CityDbEntity)

    suspend fun deleteCity(cityId: Long)

    suspend fun getAllCities(): List<CityDbEntity>
}