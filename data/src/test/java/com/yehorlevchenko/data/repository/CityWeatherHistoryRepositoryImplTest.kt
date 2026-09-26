package com.yehorlevchenko.data.repository

import com.yehorlevchenko.data.fake.FakeLocalCityWeatherHistoryDataSource
import com.yehorlevchenko.data.storage.local.entity.CityWeatherHistoryEntity
import com.yehorlevchenko.data.utils.errors.DbError
import com.yehorlevchenko.domain.entity.CityWeatherHistory
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CityWeatherHistoryRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val localDataSource = FakeLocalCityWeatherHistoryDataSource()
    private val repository = CityWeatherHistoryRepositoryImpl(dispatcher, localDataSource)

    @Test
    fun `history of the city is returned`() = runTest(dispatcher) {
        localDataSource.history[7] = listOf(
            CityWeatherHistoryEntity(id = 1, description = "Rain", temperature = 280.0, date = 1000L, iconId = "10d")
        )

        val history = repository.getCityWeatherHistoryByCityId(7).getOrThrow()

        assertEquals(
            listOf(CityWeatherHistory(id = 1, description = "Rain", temperature = 280.0, date = 1000L, iconId = "10d")),
            history
        )
    }

    @Test
    fun `empty history is returned for city without records`() = runTest(dispatcher) {
        assertTrue(repository.getCityWeatherHistoryByCityId(7).getOrThrow().isEmpty())
    }

    @Test
    fun `query failure is mapped to query error`() = runTest(dispatcher) {
        localDataSource.error = RuntimeException()

        val result = repository.getCityWeatherHistoryByCityId(7)

        assertTrue(result.exceptionOrNull() is DbError.QueryError)
    }
}
