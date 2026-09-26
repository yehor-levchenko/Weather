package com.yehorlevchenko.data.repository

import com.yehorlevchenko.core.annotations.IoDispatcher
import com.yehorlevchenko.data.mapper.toCity
import com.yehorlevchenko.data.mapper.toCityDbEntity
import com.yehorlevchenko.data.storage.local.datasource.city.LocalCityDataSource
import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.repository.CityRepository
import com.yehorlevchenko.data.utils.errors.DbError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CityRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val localCityDataSource: LocalCityDataSource
) : CityRepository {

    override suspend fun saveCity(city: City): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                localCityDataSource.saveCity(city.toCityDbEntity())
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(DbError.InsertError) }
            )
        }
    }

    override suspend fun deleteCity(cityId: Long): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                localCityDataSource.deleteCity(cityId)
            }.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(DbError.DeleteError) }
            )
        }
    }

    override suspend fun getAllCities(): Result<List<City>> {
        return withContext(ioDispatcher) {
            runCatching {
                localCityDataSource.getAllCities().map { it.toCity() }
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(DbError.QueryError) }
            )
        }
    }
}