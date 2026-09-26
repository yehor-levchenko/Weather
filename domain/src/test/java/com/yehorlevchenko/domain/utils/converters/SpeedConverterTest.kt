package com.yehorlevchenko.domain.utils.converters

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeedConverterTest {

    private val converter = SpeedConverter()

    @Test
    fun `zero speed stays zero`() {
        assertEquals(0.0, converter.convertMetersPerSecondToKilometersPerHour(0.0), DELTA)
    }

    @Test
    fun `one meter per second is 3_6 kilometers per hour`() {
        assertEquals(3.6, converter.convertMetersPerSecondToKilometersPerHour(1.0), DELTA)
    }

    @Test
    fun `fractional speed is converted correctly`() {
        assertEquals(18.72, converter.convertMetersPerSecondToKilometersPerHour(5.2), DELTA)
    }

    private companion object {
        const val DELTA = 1e-9
    }
}
