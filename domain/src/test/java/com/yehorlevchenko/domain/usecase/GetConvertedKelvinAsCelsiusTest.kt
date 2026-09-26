package com.yehorlevchenko.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class GetConvertedKelvinAsCelsiusTest {

    private val getConvertedKelvinAsCelsius = GetConvertedKelvinAsCelsius()

    @Test
    fun `result is rounded to one decimal place`() {
        assertEquals(19.9, getConvertedKelvinAsCelsius(293.0), 0.0)
    }

    @Test
    fun `result is rounded half up`() {
        assertEquals(10.9, getConvertedKelvinAsCelsius(284.0), 0.0)
    }

    @Test
    fun `negative result is rounded correctly`() {
        assertEquals(-13.0, getConvertedKelvinAsCelsius(260.12), 0.0)
    }
}
