package com.yehorlevchenko.domain.utils.formatters

import com.yehorlevchenko.domain.utils.constants.Constants.DATE_FORMAT
import com.yehorlevchenko.domain.utils.constants.Constants.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter {

    fun getDateAndTimeAsString(date: Long): String {
        return SimpleDateFormat(
            String.format("%s - %s", DATE_FORMAT, TIME_FORMAT),
            Locale.getDefault()
        ).format(Date(date))
    }
}