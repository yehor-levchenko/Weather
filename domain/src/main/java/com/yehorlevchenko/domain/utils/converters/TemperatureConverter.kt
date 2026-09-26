package com.yehorlevchenko.domain.utils.converters

import com.yehorlevchenko.domain.utils.constants.Constants

class TemperatureConverter {

    fun convertKelvinToCelsius(kelvin: Double): Double {
        return kelvin - Constants.FREEZING_POINT_OF_WATER
    }
}