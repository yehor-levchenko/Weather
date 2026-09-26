package com.yehorlevchenko.data.storage.local.db

import androidx.room.Dao
import androidx.room.Query
import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity
import com.yehorlevchenko.data.storage.local.entity.TABLE_NAME_CITY_WEATHER_DETAILS

@Dao
interface CityWeatherHistoryDao {

    @Query(
        "SELECT id, description, temperature, date, iconId " +
        "FROM $TABLE_NAME_CITY_WEATHER_DETAILS " +
        "WHERE cityId = :cityId"
    )
    suspend fun getCityWeatherHistoryByCityId(cityId: Long): List<CityWeatherHistoryEntity>
}