package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.repository.CityRepository

class DeleteCityUseCase(private val cityRepository: CityRepository) {

    suspend operator fun invoke(cityId: Long): Result<Unit> {
        return cityRepository.deleteCity(cityId)
    }
}