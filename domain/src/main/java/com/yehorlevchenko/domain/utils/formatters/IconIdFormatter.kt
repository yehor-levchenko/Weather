package com.yehorlevchenko.domain.utils.formatters

class IconIdFormatter {

    fun getIconUrlFromIconId(iconId: String): String {
        return "https://openweathermap.org/img/w/$iconId.png"
    }
}