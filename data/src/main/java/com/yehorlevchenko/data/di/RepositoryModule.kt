package com.yehorlevchenko.data.di

import com.yehorlevchenko.core.annotations.IoDispatcher
import com.yehorlevchenko.data.repository.CityRepositoryImpl
import com.yehorlevchenko.data.repository.CityWeatherDetailsRepositoryImpl
import com.yehorlevchenko.data.repository.CityWeatherHistoryRepositoryImpl
import com.yehorlevchenko.data.storage.local.datasource.city.LocalCityDataSource
import com.yehorlevchenko.data.storage.local.datasource.details.LocalCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.local.datasource.history.LocalCityWeatherHistoryDataSource
import com.yehorlevchenko.data.storage.remote.datasource.details.RemoteCityWeatherDetailsDataSource
import com.yehorlevchenko.domain.repository.CityRepository
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository
import com.yehorlevchenko.domain.repository.CityWeatherHistoryRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCityRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        localCityDataSource: LocalCityDataSource
    ): CityRepository {
        return CityRepositoryImpl(ioDispatcher, localCityDataSource)
    }

    @Provides
    @Singleton
    fun provideCityWeatherDetailsRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        localCityWeatherDetailsDataSource: LocalCityWeatherDetailsDataSource,
        remoteCityWeatherDetailsDataSource: RemoteCityWeatherDetailsDataSource
    ): CityWeatherDetailsRepository {
        return CityWeatherDetailsRepositoryImpl(
            ioDispatcher,
            localCityWeatherDetailsDataSource,
            remoteCityWeatherDetailsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideCityWeatherHistoryRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        localCityWeatherHistoryDataSource: LocalCityWeatherHistoryDataSource
    ): CityWeatherHistoryRepository {
        return CityWeatherHistoryRepositoryImpl(
            ioDispatcher,
            localCityWeatherHistoryDataSource
        )
    }
}