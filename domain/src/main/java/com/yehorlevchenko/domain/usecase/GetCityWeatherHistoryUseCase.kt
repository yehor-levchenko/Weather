package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.entity.CityWeatherHistory
import com.yehorlevchenko.domain.repository.CityWeatherHistoryRepository

class GetCityWeatherHistoryUseCase(
    private val cityWeatherHistoryRepository: CityWeatherHistoryRepository
) {

    suspend operator fun invoke(cityId: Long): Result<List<CityWeatherHistory>> {
        return cityWeatherHistoryRepository.getCityWeatherHistoryByCityId(cityId)
    }
}