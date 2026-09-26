package com.yehorlevchenko.data.storage.local.datasource.city

import com.yehorlevchenko.data.storage.local.db.CityDao
import com.yehorlevchenko.data.storage.local.entity.CityDbEntity

class LocalCityDataSourceImpl(
    private val cityDao: CityDao
) : LocalCityDataSource {

    override suspend fun saveCity(city: CityDbEntity) {
        cityDao.saveCity(city)
    }

    override suspend fun deleteCity(cityId: Long) {
        cityDao.deleteCity(cityId)
    }

    override suspend fun getAllCities(): List<CityDbEntity> {
        return cityDao.getAllCities()
    }
}