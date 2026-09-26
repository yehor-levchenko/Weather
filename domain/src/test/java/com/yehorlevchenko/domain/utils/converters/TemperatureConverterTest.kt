package com.yehorlevchenko.domain.utils.converters

import org.junit.Assert.assertEquals
import org.junit.Test

class TemperatureConverterTest {

    private val converter = TemperatureConverter()

    @Test
    fun `freezing point in kelvin is zero celsius`() {
        assertEquals(0.0, converter.convertKelvinToCelsius(273.15), DELTA)
    }

    @Test
    fun `absolute zero is minus 273_15 celsius`() {
        assertEquals(-273.15, converter.convertKelvinToCelsius(0.0), DELTA)
    }

    @Test
    fun `positive temperature is converted correctly`() {
        assertEquals(25.0, converter.convertKelvinToCelsius(298.15), DELTA)
    }

    private companion object {
        const val DELTA = 1e-9
    }
}
