package com.yehorlevchenko.data.storage.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yehorlevchenko.data.storage.local.entity.CityDbEntity
import com.yehorlevchenko.data.storage.local.entity.TABLE_NAME_CITY

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCity(city: CityDbEntity)

    @Query("DELETE FROM $TABLE_NAME_CITY WHERE id = :cityId")
    suspend fun deleteCity(cityId: Long)

    @Query("SELECT * FROM $TABLE_NAME_CITY")
    suspend fun getAllCities(): List<CityDbEntity>
}