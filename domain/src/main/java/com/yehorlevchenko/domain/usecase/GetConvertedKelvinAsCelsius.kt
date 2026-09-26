package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.utils.converters.TemperatureConverter
import java.math.BigDecimal
import java.math.RoundingMode

class GetConvertedKelvinAsCelsius {

    operator fun invoke(kelvin: Double): Double {
        val celsius = TemperatureConverter().convertKelvinToCelsius(kelvin)
        return BigDecimal(celsius).setScale(1, RoundingMode.HALF_UP).toDouble()
    }
}