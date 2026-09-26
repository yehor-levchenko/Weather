package com.yehorlevchenko.presentation.ui.screen.history

import com.yehorlevchenko.domain.utils.types.DataSourceType

sealed interface CityWeatherHistoryEvent {

    sealed interface UserEvent {
        data object OnBackClick : UserEvent
        class OnScreenOpen(val cityId: Long, val cityName: String) : UserEvent
        class OnCityWeatherHistoryItemClick(val cityWeatherId: Long) : UserEvent
        class OnEventPerformed(val event: Event) : UserEvent
    }

    sealed interface Event {
        data object NavigateBack : Event
        class NavigateToCityWeatherDetails(
            val dataSourceType: DataSourceType,
            val cityWeatherId: Long,
            val cityName: String
        ) : Event
    }
}