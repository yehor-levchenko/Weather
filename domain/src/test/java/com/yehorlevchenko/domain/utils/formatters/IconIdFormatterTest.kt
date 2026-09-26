package com.yehorlevchenko.domain.utils.formatters

import org.junit.Assert.assertEquals
import org.junit.Test

class IconIdFormatterTest {

    @Test
    fun `icon id is converted to openweathermap icon url`() {
        assertEquals(
            "https://openweathermap.org/img/w/10d.png",
            IconIdFormatter().getIconUrlFromIconId("10d")
        )
    }
}
