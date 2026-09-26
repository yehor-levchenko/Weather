package com.yehorlevchenko.data.di

import com.yehorlevchenko.data.storage.local.datasource.city.LocalCityDataSource
import com.yehorlevchenko.data.storage.local.datasource.city.LocalCityDataSourceImpl
import com.yehorlevchenko.data.storage.local.datasource.details.LocalCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.local.datasource.details.LocalCityWeatherDetailsDataSourceImpl
import com.yehorlevchenko.data.storage.local.datasource.history.LocalCityWeatherHistoryDataSource
import com.yehorlevchenko.data.storage.local.datasource.history.LocalCityWeatherHistoryDataSourceImpl
import com.yehorlevchenko.data.storage.local.db.CityDao
import com.yehorlevchenko.data.storage.local.db.CityWeatherDetailsDao
import com.yehorlevchenko.data.storage.local.db.CityWeatherHistoryDao
import com.yehorlevchenko.data.storage.remote.api.CityWeatherDetailsApi
import com.yehorlevchenko.data.storage.remote.datasource.details.RemoteCityWeatherDetailsDataSource
import com.yehorlevchenko.data.storage.remote.datasource.details.RemoteCityWeatherDetailsDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalCityDataSource(
        cityDao: CityDao
    ): LocalCityDataSource {
        return LocalCityDataSourceImpl(cityDao)
    }

    @Provides
    @Singleton
    fun provideLocalCityWeatherDetailsDataSource(
        cityWeatherDetailsDao: CityWeatherDetailsDao
    ): LocalCityWeatherDetailsDataSource {
        return LocalCityWeatherDetailsDataSourceImpl(cityWeatherDetailsDao)
    }

    @Provides
    @Singleton
    fun provideLocalCityWeatherHistoryDataSource(
        cityWeatherHistoryDao: CityWeatherHistoryDao
    ): LocalCityWeatherHistoryDataSource {
        return LocalCityWeatherHistoryDataSourceImpl(cityWeatherHistoryDao)
    }

    @Provides
    @Singleton
    fun provideRemoteCityWeatherDetailsDataSource(
        cityWeatherDetailsApi: CityWeatherDetailsApi
    ): RemoteCityWeatherDetailsDataSource {
        return RemoteCityWeatherDetailsDataSourceImpl(cityWeatherDetailsApi)
    }
}