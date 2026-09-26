package com.yehorlevchenko.presentation.ui.screen.details

import com.yehorlevchenko.domain.utils.types.DataSourceType

sealed interface CityWeatherDetailsEvent {

    sealed interface UserEvent {
        data object OnBackClick : UserEvent
        class OnEventPerformed(val event: Event) : UserEvent
        class OnScreenOpen(
            val dataSourceType: DataSourceType,
            val cityWeatherId: Long,
            val cityName: String,
            val cityId: Long
        ) : UserEvent
    }

    sealed interface Event {
        data object NavigateBack : Event
    }
}