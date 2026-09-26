package com.yehorlevchenko.presentation.fake

import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.repository.CityRepository

class FakeCityRepository : CityRepository {

    val cities = mutableListOf<City>()
    var error: Throwable? = null

    override suspend fun saveCity(city: City): Result<Unit> = result {
        cities += city.copy(id = (cities.maxOfOrNull { it.id } ?: 0) + 1)
    }

    override suspend fun deleteCity(cityId: Long): Result<Unit> = result {
        cities.removeAll { it.id == cityId }
    }

    override suspend fun getAllCities(): Result<List<City>> = result { cities.toList() }

    private fun <T> result(block: () -> T): Result<T> =
        error?.let { Result.failure(it) } ?: Result.success(block())
}
