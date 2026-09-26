package com.yehorlevchenko.data.repository

import com.yehorlevchenko.core.annotations.IoDispatcher
import com.yehorlevchenko.data.mapper.toCityWeatherDetails
import com.yehorlevchenko.data.mapper.toCityWeatherDetailsDbEntity
import com.yehorlevchenko.data.storage.local.datasource.details.LocalCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.remote.datasource.details.RemoteCityWeatherDetailsDataSource
import com.yehorlevchenko.data.utils.constants.HttpErrorCodes
import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository
import com.yehorlevchenko.data.utils.errors.ApiError
import com.yehorlevchenko.data.utils.errors.DbError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Calendar

class CityWeatherDetailsRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val localCityWeatherDetailsDataSource: LocalCityWeatherDetailsDataSource,
    private val remoteCityWeatherDetailsDataSource: RemoteCityWeatherDetailsDataSource
) : CityWeatherDetailsRepository {

    override suspend fun getStoredCityWeatherDetailsByCityWeatherId(
        cityWeatherId: Long
    ): Result<CityWeatherDetails> {
        return withContext(ioDispatcher) {
            runCatching {
                localCityWeatherDetailsDataSource.getStoredCityWeatherDetailsByCityWeatherId(cityWeatherId)
                    ?.toCityWeatherDetails()
                    ?: throw DbError.QueryError
            }.fold(
                onSuccess = { Result.success(it) },
                onFailure = { Result.failure(DbError.QueryError) }
            )
        }
    }

    override suspend fun getActualCityWeatherDetailsByCityName(
        cityName: String,
        cityId: Long
    ): Result<CityWeatherDetails> {
        return runCatching {
            remoteCityWeatherDetailsDataSource.getActualCityWeatherDetailsByCityName(cityName)
        }.fold(
            onSuccess = { details ->
                val cityWeatherDetails = details.toCityWeatherDetails(Calendar.getInstance().timeInMillis)
                runCatching {
                    val cityWeatherDetailsDbEntity = cityWeatherDetails.toCityWeatherDetailsDbEntity(cityId)
                    localCityWeatherDetailsDataSource.saveCityWeatherDetails(cityWeatherDetailsDbEntity)
                }.fold(
                    onSuccess = { Result.success(cityWeatherDetails) },
                    onFailure = { Result.failure(DbError.InsertError) }
                )
            },
            onFailure = {
                when (it) {
                    is HttpException -> {
                        when (it.code()) {
                            HttpErrorCodes.UNAUTHORIZED -> Result.failure(ApiError.Unauthorized)
                            HttpErrorCodes.NOT_FOUND -> Result.failure(ApiError.NotFound)
                            HttpErrorCodes.INTERNAL_SERVER_ERROR -> Result.failure(ApiError.InternalServerError)
                            else -> Result.failure(ApiError.HttpError)
                        }
                    }
                    else -> Result.failure(ApiError.NetworkError)
                }
            }
        )
    }
}