package com.yehorlevchenko.data.storage.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yehorlevchenko.data.storage.local.entity.CityDbEntity
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity

@Database(
    entities = [
        CityDbEntity::class,
        CityWeatherDetailsDbEntity::class
    ],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun cityWeatherDetailsDao(): CityWeatherDetailsDao

    abstract fun cityWeatherHistoryDao(): CityWeatherHistoryDao
}