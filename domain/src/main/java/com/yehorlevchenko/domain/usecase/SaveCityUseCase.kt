package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.repository.CityRepository

class SaveCityUseCase(private val cityRepository: CityRepository) {

    suspend operator fun invoke(city: City): Result<Unit> {
        return cityRepository.saveCity(city)
    }
}