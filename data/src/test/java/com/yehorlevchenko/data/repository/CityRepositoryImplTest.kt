package com.yehorlevchenko.data.repository

import com.yehorlevchenko.data.fake.FakeLocalCityDataSource
import com.yehorlevchenko.data.utils.errors.DbError
import com.yehorlevchenko.domain.entity.City
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CityRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val localDataSource = FakeLocalCityDataSource()
    private val repository = CityRepositoryImpl(dispatcher, localDataSource)

    @Test
    fun `saved city is returned in all cities`() = runTest(dispatcher) {
        repository.saveCity(City(id = 0, name = "Kyiv"))

        assertEquals(listOf(City(id = 1, name = "Kyiv")), repository.getAllCities().getOrThrow())
    }

    @Test
    fun `deleted city is not returned in all cities`() = runTest(dispatcher) {
        repository.saveCity(City(id = 0, name = "Kyiv"))
        repository.saveCity(City(id = 0, name = "Lviv"))

        repository.deleteCity(cityId = 1)

        assertEquals(listOf(City(id = 2, name = "Lviv")), repository.getAllCities().getOrThrow())
    }

    @Test
    fun `save failure is mapped to insert error`() = runTest(dispatcher) {
        localDataSource.error = RuntimeException()

        val result = repository.saveCity(City(id = 0, name = "Kyiv"))

        assertTrue(result.exceptionOrNull() is DbError.InsertError)
    }

    @Test
    fun `delete failure is mapped to delete error`() = runTest(dispatcher) {
        localDataSource.error = RuntimeException()

        val result = repository.deleteCity(cityId = 1)

        assertTrue(result.exceptionOrNull() is DbError.DeleteError)
    }

    @Test
    fun `query failure is mapped to query error`() = runTest(dispatcher) {
        localDataSource.error = RuntimeException()

        val result = repository.getAllCities()

        assertTrue(result.exceptionOrNull() is DbError.QueryError)
    }
}
