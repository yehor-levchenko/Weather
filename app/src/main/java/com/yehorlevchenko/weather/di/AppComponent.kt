package com.yehorlevchenko.weather.di

import android.content.Context
import com.yehorlevchenko.core.di.CoroutineDispatcherModule
import com.yehorlevchenko.data.di.DataSourceModule
import com.yehorlevchenko.data.di.DatabaseModule
import com.yehorlevchenko.data.di.NetworkModule
import com.yehorlevchenko.data.di.RepositoryModule
import com.yehorlevchenko.data.di.UseCaseModule
import com.yehorlevchenko.presentation.di.PresentationAppComponent
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesViewModel
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsViewModel
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DatabaseModule::class,
        DataSourceModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        CoroutineDispatcherModule::class
    ]
)
interface AppComponent : PresentationAppComponent {

    override fun injectCitiesViewModel(
        citiesViewModel: CitiesViewModel
    )

    override fun injectCityWeatherDetailsViewModel(
        cityWeatherDetailsViewModel: CityWeatherDetailsViewModel
    )

    override fun injectCityWeatherHistoryViewModel(
        cityWeatherHistoryViewModel: CityWeatherHistoryViewModel
    )

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}