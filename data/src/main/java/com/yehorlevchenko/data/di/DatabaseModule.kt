package com.yehorlevchenko.data.di

import android.content.Context
import androidx.room.Room
import com.yehorlevchenko.data.storage.local.db.CityDao
import com.yehorlevchenko.data.storage.local.db.CityWeatherDetailsDao
import com.yehorlevchenko.data.storage.local.db.CityWeatherHistoryDao
import com.yehorlevchenko.data.storage.local.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideCityDatabase(context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCityDao(weatherDatabase: WeatherDatabase): CityDao {
        return weatherDatabase.cityDao()
    }

    @Singleton
    @Provides
    fun provideCityWeatherDetailsDao(weatherDatabase: WeatherDatabase): CityWeatherDetailsDao {
        return weatherDatabase.cityWeatherDetailsDao()
    }

    @Singleton
    @Provides
    fun provideCityWeatherHistoryDao(weatherDatabase: WeatherDatabase): CityWeatherHistoryDao {
        return weatherDatabase.cityWeatherHistoryDao()
    }
}