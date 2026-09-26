package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository

class GetStoredCityWeatherDetailsUseCase(
    private val cityWeatherDetailsRepository: CityWeatherDetailsRepository
) {

    suspend operator fun invoke(cityWeatherId: Long): Result<CityWeatherDetails> {
        return cityWeatherDetailsRepository.getStoredCityWeatherDetailsByCityWeatherId(cityWeatherId)
    }
}