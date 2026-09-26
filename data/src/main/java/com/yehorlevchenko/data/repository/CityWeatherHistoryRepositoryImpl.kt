package com.yehorlevchenko.data.repository

import com.yehorlevchenko.core.annotations.IoDispatcher
import com.yehorlevchenko.data.mapper.toCityWeatherHistory
import com.yehorlevchenko.data.storage.local.datasource.history.LocalCityWeatherHistoryDataSource
import com.yehorlevchenko.domain.entity.CityWeatherHistory
import com.yehorlevchenko.domain.repository.CityWeatherHistoryRepository
import com.yehorlevchenko.data.utils.errors.DbError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CityWeatherHistoryRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val localCityWeatherHistoryDataSource: LocalCityWeatherHistoryDataSource
) : CityWeatherHistoryRepository {

    override suspend fun getCityWeatherHistoryByCityId(
        cityId: Long
    ): Result<List<CityWeatherHistory>> {
        return withContext(ioDispatcher) {
            runCatching {
                val history = localCityWeatherHistoryDataSource.getCityWeatherHistoryByCityId(cityId)
                history.map { it.toCityWeatherHistory() }
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(DbError.QueryError) }
            )
        }
    }
}