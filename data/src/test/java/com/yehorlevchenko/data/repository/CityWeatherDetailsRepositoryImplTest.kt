package com.yehorlevchenko.data.repository

import com.yehorlevchenko.data.fake.FakeLocalCityWeatherDetailsDataSource
import com.yehorlevchenko.data.fake.FakeRemoteCityWeatherDetailsDataSource
import com.yehorlevchenko.data.fake.createCityWeatherDetailsApiEntity
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity
import com.yehorlevchenko.data.utils.errors.ApiError
import com.yehorlevchenko.data.utils.errors.DbError
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class CityWeatherDetailsRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val localDataSource = FakeLocalCityWeatherDetailsDataSource()
    private val remoteDataSource = FakeRemoteCityWeatherDetailsDataSource()
    private val repository = CityWeatherDetailsRepositoryImpl(
        dispatcher,
        localDataSource,
        remoteDataSource
    )

    @Test
    fun `actual details are requested by city name and returned`() = runTest(dispatcher) {
        remoteDataSource.response = createCityWeatherDetailsApiEntity()

        val details = repository.getActualCityWeatherDetailsByCityName("Kyiv", cityId = 7).getOrThrow()

        assertEquals(listOf("Kyiv"), remoteDataSource.requestedCityNames)
        assertEquals("Clouds", details.description)
        assertEquals(285.5, details.temperature, 0.0)
        assertEquals(72, details.humidity)
        assertEquals(4.1, details.windSpeed, 0.0)
        assertEquals("04d", details.iconId)
    }

    @Test
    fun `actual details are saved to history of the city`() = runTest(dispatcher) {
        remoteDataSource.response = createCityWeatherDetailsApiEntity()

        repository.getActualCityWeatherDetailsByCityName("Kyiv", cityId = 7)

        assertEquals(1, localDataSource.details.size)
        assertEquals(7, localDataSource.details.single().cityId)
        assertEquals("Clouds", localDataSource.details.single().description)
    }

    @Test
    fun `failure to save actual details is mapped to insert error`() = runTest(dispatcher) {
        remoteDataSource.response = createCityWeatherDetailsApiEntity()
        localDataSource.error = RuntimeException()

        val result = repository.getActualCityWeatherDetailsByCityName("Kyiv", cityId = 7)

        assertTrue(result.exceptionOrNull() is DbError.InsertError)
    }

    @Test
    fun `http 401 is mapped to unauthorized error`() = runTest(dispatcher) {
        assertHttpCodeIsMappedTo(401, ApiError.Unauthorized)
    }

    @Test
    fun `http 404 is mapped to not found error`() = runTest(dispatcher) {
        assertHttpCodeIsMappedTo(404, ApiError.NotFound)
    }

    @Test
    fun `http 500 is mapped to internal server error`() = runTest(dispatcher) {
        assertHttpCodeIsMappedTo(500, ApiError.InternalServerError)
    }

    @Test
    fun `other http codes are mapped to generic http error`() = runTest(dispatcher) {
        assertHttpCodeIsMappedTo(429, ApiError.HttpError)
    }

    @Test
    fun `non http failure is mapped to network error`() = runTest(dispatcher) {
        remoteDataSource.error = IOException()

        val result = repository.getActualCityWeatherDetailsByCityName("Kyiv", cityId = 7)

        assertEquals(ApiError.NetworkError, result.exceptionOrNull())
        assertTrue(localDataSource.details.isEmpty())
    }

    @Test
    fun `stored details are returned by id`() = runTest(dispatcher) {
        localDataSource.details += createDbEntity(id = 3)

        val details = repository.getStoredCityWeatherDetailsByCityWeatherId(3).getOrThrow()

        assertEquals(3, details.id)
        assertEquals(7, details.cityId)
    }

    @Test
    fun `missing stored details are mapped to query error`() = runTest(dispatcher) {
        val result = repository.getStoredCityWeatherDetailsByCityWeatherId(3)

        assertTrue(result.exceptionOrNull() is DbError.QueryError)
    }

    private suspend fun assertHttpCodeIsMappedTo(code: Int, expected: ApiError) {
        remoteDataSource.error = HttpException(Response.error<Any>(code, ResponseBody.create(null, "")))

        val result = repository.getActualCityWeatherDetailsByCityName("Kyiv", cityId = 7)

        assertEquals(expected, result.exceptionOrNull())
    }

    private fun createDbEntity(id: Long) = CityWeatherDetailsDbEntity(
        id = id,
        cityId = 7,
        description = "Rain",
        temperature = 280.0,
        humidity = 90,
        windSpeed = 6.0,
        date = 2000L,
        iconId = "10d"
    )
}
