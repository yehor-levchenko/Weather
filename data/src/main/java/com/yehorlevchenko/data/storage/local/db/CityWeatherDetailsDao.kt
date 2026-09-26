package com.yehorlevchenko.data.storage.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity
import com.yehorlevchenko.data.storage.local.entity.TABLE_NAME_CITY_WEATHER_DETAILS

@Dao
interface CityWeatherDetailsDao {

    @Query("SELECT * FROM $TABLE_NAME_CITY_WEATHER_DETAILS WHERE id = :cityWeatherId")
    suspend fun getStoredCityWeatherDetailsByCityWeatherId(cityWeatherId: Long): CityWeatherDetailsDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCityWeatherDetails(cityWeatherDetails: CityWeatherDetailsDbEntity)
}