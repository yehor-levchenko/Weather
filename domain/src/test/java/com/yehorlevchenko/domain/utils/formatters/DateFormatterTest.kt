package com.yehorlevchenko.domain.utils.formatters

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class DateFormatterTest {

    private val formatter = DateFormatter()
    private lateinit var defaultTimeZone: TimeZone
    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultTimeZone = TimeZone.getDefault()
        defaultLocale = Locale.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(defaultTimeZone)
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `date is formatted with month and 24-hour time`() {
        // 2024-10-02 21:35:41 UTC
        assertEquals("02.10.2024 - 21:35", formatter.getDateAndTimeAsString(1727904941000L))
    }

    @Test
    fun `epoch start is formatted correctly`() {
        assertEquals("01.01.1970 - 00:00", formatter.getDateAndTimeAsString(0L))
    }
}
