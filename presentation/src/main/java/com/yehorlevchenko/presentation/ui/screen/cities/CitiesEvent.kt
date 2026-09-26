package com.yehorlevchenko.presentation.ui.screen.cities

import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.entity.CityScreenEntity

sealed interface CitiesEvent {

    sealed interface UserEvent {
        data object OnScreenOpen : UserEvent
        data object OnAddCityClick : UserEvent
        class OnAddCityConfirmClick(val cityName: String) : UserEvent
        class OnSwipeToDeleteCity(val cityId: Long) : UserEvent
        class OnCityItemClick(val city: CityScreenEntity) : UserEvent
        class OnCityWeatherHistoryIconClick(val city: CityScreenEntity) : UserEvent
        class OnEventPerformed(val event: Event) : UserEvent
    }

    sealed interface Event {
        data object ShowAddCityView : Event
        data object HideAddCityView : Event
        class NavigateToCityWeatherHistory(val city: CityScreenEntity) : Event
        class NavigateToCityWeatherDetails(
            val dataSourceType: DataSourceType,
            val city: CityScreenEntity
        ) : Event
    }
}