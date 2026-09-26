package com.yehorlevchenko.presentation.di

import com.yehorlevchenko.presentation.ui.screen.cities.CitiesViewModel
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsViewModel
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryViewModel

interface PresentationAppComponent {

    fun injectCitiesViewModel(
        citiesViewModel: CitiesViewModel
    )

    fun injectCityWeatherDetailsViewModel(
        cityWeatherDetailsViewModel: CityWeatherDetailsViewModel
    )

    fun injectCityWeatherHistoryViewModel(
        cityWeatherHistoryViewModel: CityWeatherHistoryViewModel
    )
}