package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository

class GetActualCityWeatherDetailsUseCase(
    private val cityWeatherDetailsRepository: CityWeatherDetailsRepository
) {

    suspend operator fun invoke(cityName: String, cityId: Long): Result<CityWeatherDetails> {
        return cityWeatherDetailsRepository.getActualCityWeatherDetailsByCityName(cityName, cityId)
    }
}