package com.yehorlevchenko.data.mapper

import com.yehorlevchenko.data.fake.createCityWeatherDetailsApiEntity
import com.yehorlevchenko.data.storage.local.entity.CityWeatherDetailsDbEntity
import com.yehorlevchenko.domain.entity.CityWeatherDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class CityWeatherDetailsMapperTest {

    @Test
    fun `api entity is mapped to domain entity`() {
        val details = createCityWeatherDetailsApiEntity().toCityWeatherDetails(date = 1000L)

        assertEquals(
            CityWeatherDetails(
                id = 0,
                cityId = 0,
                description = "Clouds",
                temperature = 285.5,
                humidity = 72,
                windSpeed = 4.1,
                date = 1000L,
                iconId = "04d"
            ),
            details
        )
    }

    @Test
    fun `domain entity is mapped to db entity with given city id`() {
        val details = createCityWeatherDetailsApiEntity().toCityWeatherDetails(date = 1000L)

        val dbEntity = details.toCityWeatherDetailsDbEntity(cityId = 7)

        assertEquals(7, dbEntity.cityId)
        assertEquals(details.copy(cityId = 7), dbEntity.toCityWeatherDetails())
    }

    @Test
    fun `db entity is mapped to domain entity`() {
        val dbEntity = CityWeatherDetailsDbEntity(
            id = 3,
            cityId = 7,
            description = "Rain",
            temperature = 280.0,
            humidity = 90,
            windSpeed = 6.0,
            date = 2000L,
            iconId = "10d"
        )

        val details = dbEntity.toCityWeatherDetails()

        assertEquals(3, details.id)
        assertEquals(7, details.cityId)
        assertEquals("Rain", details.description)
        assertEquals(280.0, details.temperature, 0.0)
        assertEquals(90, details.humidity)
        assertEquals(6.0, details.windSpeed, 0.0)
        assertEquals(2000L, details.date)
        assertEquals("10d", details.iconId)
    }
}
