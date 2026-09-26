package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.utils.converters.SpeedConverter
import java.math.BigDecimal
import java.math.RoundingMode

class GetConvertedMetersPerSecondAsKilometersPerHour {

    operator fun invoke(metersPerSecond: Double): Double {
        val kilometersPerHour = SpeedConverter().convertMetersPerSecondToKilometersPerHour(metersPerSecond)
        return BigDecimal(kilometersPerHour).setScale(1, RoundingMode.HALF_UP).toDouble()
    }
}