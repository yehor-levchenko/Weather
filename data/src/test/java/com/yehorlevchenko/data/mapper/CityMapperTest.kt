package com.yehorlevchenko.data.mapper

import com.yehorlevchenko.domain.entity.City
import org.junit.Assert.assertEquals
import org.junit.Test

class CityMapperTest {

    @Test
    fun `city survives mapping to db entity and back`() {
        val city = City(id = 5, name = "Kyiv")

        assertEquals(city, city.toCityDbEntity().toCity())
    }
}
