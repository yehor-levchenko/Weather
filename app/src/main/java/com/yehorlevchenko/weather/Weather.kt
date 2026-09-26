package com.yehorlevchenko.weather

import android.app.Application
import com.yehorlevchenko.presentation.di.PresentationAppComponent
import com.yehorlevchenko.presentation.di.PresentationAppComponentProvider
import com.yehorlevchenko.weather.di.AppComponent
import com.yehorlevchenko.weather.di.DaggerAppComponent

class Weather : Application(), PresentationAppComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
    }

    override fun getPresentationAppComponent(): PresentationAppComponent {
        return appComponent
    }
}