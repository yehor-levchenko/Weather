package com.yehorlevchenko.data.di

import com.yehorlevchenko.domain.repository.CityRepository
import com.yehorlevchenko.domain.repository.CityWeatherDetailsRepository
import com.yehorlevchenko.domain.repository.CityWeatherHistoryRepository
import com.yehorlevchenko.domain.usecase.DeleteCityUseCase
import com.yehorlevchenko.domain.usecase.GetActualCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.usecase.GetAllCitiesUseCase
import com.yehorlevchenko.domain.usecase.GetCityWeatherHistoryUseCase
import com.yehorlevchenko.domain.usecase.GetConvertedIconIdAsIconUrl
import com.yehorlevchenko.domain.usecase.GetConvertedKelvinAsCelsius
import com.yehorlevchenko.domain.usecase.GetConvertedMetersPerSecondAsKilometersPerHour
import com.yehorlevchenko.domain.usecase.GetFormattedDateAndTime
import com.yehorlevchenko.domain.usecase.GetStoredCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.usecase.SaveCityUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideSaveCityUseCase(
        cityRepository: CityRepository
    ): SaveCityUseCase {
        return SaveCityUseCase(cityRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteCityUseCase(
        cityRepository: CityRepository
    ): DeleteCityUseCase {
        return DeleteCityUseCase(cityRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllCitiesUseCase(
        cityRepository: CityRepository
    ): GetAllCitiesUseCase {
        return GetAllCitiesUseCase(cityRepository)
    }

    @Provides
    @Singleton
    fun provideGetActualCityWeatherDetailsUseCase(
        cityWeatherDetailsRepository: CityWeatherDetailsRepository
    ): GetActualCityWeatherDetailsUseCase {
        return GetActualCityWeatherDetailsUseCase(cityWeatherDetailsRepository)
    }

    @Provides
    @Singleton
    fun provideGetCityWeatherHistoryUseCase(
        cityWeatherHistoryRepository: CityWeatherHistoryRepository
    ): GetCityWeatherHistoryUseCase {
        return GetCityWeatherHistoryUseCase(cityWeatherHistoryRepository)
    }

    @Provides
    @Singleton
    fun provideGetStoredCityWeatherDetailsUseCase(
        cityWeatherDetailsRepository: CityWeatherDetailsRepository
    ): GetStoredCityWeatherDetailsUseCase {
        return GetStoredCityWeatherDetailsUseCase(cityWeatherDetailsRepository)
    }

    @Provides
    @Singleton
    fun provideGetConvertedKelvinAsCelsius(): GetConvertedKelvinAsCelsius {
        return GetConvertedKelvinAsCelsius()
    }

    @Provides
    @Singleton
    fun provideGetFormattedDateAndTime(): GetFormattedDateAndTime {
        return GetFormattedDateAndTime()
    }

    @Provides
    @Singleton
    fun provideGetConvertedMetersPerSecondAsKilometersPerHour(): GetConvertedMetersPerSecondAsKilometersPerHour {
        return GetConvertedMetersPerSecondAsKilometersPerHour()
    }

    @Provides
    @Singleton
    fun provideGetConvertedIconIdAsIconUrl(): GetConvertedIconIdAsIconUrl {
        return GetConvertedIconIdAsIconUrl()
    }
}