package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.repository.CityRepository

class GetAllCitiesUseCase(private val cityRepository: CityRepository) {

    suspend operator fun invoke(): Result<List<City>> {
        return cityRepository.getAllCities()
    }
}