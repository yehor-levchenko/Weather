package com.yehorlevchenko.domain.utils.converters

import com.yehorlevchenko.domain.utils.constants.Constants

class SpeedConverter {

    fun convertMetersPerSecondToKilometersPerHour(metersPerSecond: Double): Double {
        return metersPerSecond * Constants.METERS_PER_SECOND_CONVERSION_FACTOR
    }
}