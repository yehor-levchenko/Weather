package com.yehorlevchenko.domain.repository

import com.yehorlevchenko.domain.entity.City

interface CityRepository {

    suspend fun saveCity(city: City): Result<Unit>

    suspend fun deleteCity(cityId: Long): Result<Unit>

    suspend fun getAllCities(): Result<List<City>>
}