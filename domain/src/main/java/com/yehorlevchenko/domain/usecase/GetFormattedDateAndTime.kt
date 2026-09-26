package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.utils.formatters.DateFormatter

class GetFormattedDateAndTime {

    operator fun invoke(date: Long): String {
        return DateFormatter().getDateAndTimeAsString(date)
    }
}