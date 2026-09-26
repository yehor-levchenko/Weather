package com.yehorlevchenko.data.di

import com.yehorlevchenko.data.storage.remote.api.CityWeatherDetailsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCityWeatherDetailsApiService(retrofit: Retrofit): CityWeatherDetailsApi {
        return retrofit.create(CityWeatherDetailsApi::class.java)
    }
}