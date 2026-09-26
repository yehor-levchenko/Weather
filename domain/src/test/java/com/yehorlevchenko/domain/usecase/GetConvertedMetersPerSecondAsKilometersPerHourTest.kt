package com.yehorlevchenko.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class GetConvertedMetersPerSecondAsKilometersPerHourTest {

    private val getConvertedMetersPerSecondAsKilometersPerHour =
        GetConvertedMetersPerSecondAsKilometersPerHour()

    @Test
    fun `result is rounded to one decimal place`() {
        assertEquals(18.7, getConvertedMetersPerSecondAsKilometersPerHour(5.2), 0.0)
    }

    @Test
    fun `whole result is kept as is`() {
        assertEquals(36.0, getConvertedMetersPerSecondAsKilometersPerHour(10.0), 0.0)
    }
}
