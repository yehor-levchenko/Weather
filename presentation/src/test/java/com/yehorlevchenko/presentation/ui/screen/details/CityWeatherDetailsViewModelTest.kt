package com.yehorlevchenko.presentation.ui.screen.details

import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.usecase.GetActualCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.usecase.GetConvertedIconIdAsIconUrl
import com.yehorlevchenko.domain.usecase.GetConvertedKelvinAsCelsius
import com.yehorlevchenko.domain.usecase.GetConvertedMetersPerSecondAsKilometersPerHour
import com.yehorlevchenko.domain.usecase.GetFormattedDateAndTime
import com.yehorlevchenko.domain.usecase.GetStoredCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.entity.CityWeatherDetailsScreenEntity
import com.yehorlevchenko.presentation.fake.FakeCityWeatherDetailsRepository
import com.yehorlevchenko.presentation.rule.MainDispatcherRule
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.Event
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.UserEvent
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class CityWeatherDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCityWeatherDetailsRepository()
    private lateinit var viewModel: CityWeatherDetailsViewModel
    private lateinit var defaultTimeZone: TimeZone
    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultTimeZone = TimeZone.getDefault()
        defaultLocale = Locale.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)

        viewModel = CityWeatherDetailsViewModel().apply {
            getActualCityWeatherDetailsUseCase = GetActualCityWeatherDetailsUseCase(repository)
            getStoredCityWeatherDetailsUseCase = GetStoredCityWeatherDetailsUseCase(repository)
            getConvertedKelvinAsCelsius = GetConvertedKelvinAsCelsius()
            getFormattedDateAndTime = GetFormattedDateAndTime()
            getConvertedMetersPerSecondAsKilometersPerHour = GetConvertedMetersPerSecondAsKilometersPerHour()
            getConvertedIconIdAsIconUrl = GetConvertedIconIdAsIconUrl()
        }
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(defaultTimeZone)
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `actual details are loaded and converted for remote source`() {
        repository.actualDetails = DETAILS

        viewModel.onUserEvent(openScreen(DataSourceType.REMOTE))

        assertEquals(ScreenDataState.Success(EXPECTED_SCREEN_ENTITY), viewModel.uiState.value.data)
    }

    @Test
    fun `stored details are loaded and converted for local source`() {
        repository.storedDetails[DETAILS.id] = DETAILS

        viewModel.onUserEvent(openScreen(DataSourceType.LOCAL))

        assertEquals(ScreenDataState.Success(EXPECTED_SCREEN_ENTITY), viewModel.uiState.value.data)
    }

    @Test
    fun `error is shown when details cannot be loaded`() {
        val error = RuntimeException()
        repository.error = error

        viewModel.onUserEvent(openScreen(DataSourceType.REMOTE))

        assertEquals(ScreenDataState.Error(error), viewModel.uiState.value.data)
    }

    @Test
    fun `back click navigates back`() {
        viewModel.onUserEvent(UserEvent.OnBackClick)

        assertEquals(Event.NavigateBack, viewModel.uiState.value.event)
    }

    @Test
    fun `performed event is removed from state`() {
        viewModel.onUserEvent(UserEvent.OnBackClick)

        viewModel.onUserEvent(UserEvent.OnEventPerformed(Event.NavigateBack))

        assertNull(viewModel.uiState.value.event)
    }

    private fun openScreen(dataSourceType: DataSourceType) = UserEvent.OnScreenOpen(
        dataSourceType = dataSourceType,
        cityWeatherId = DETAILS.id,
        cityName = "Kyiv",
        cityId = DETAILS.cityId
    )

    private companion object {
        val DETAILS = CityWeatherDetails(
            id = 3,
            cityId = 7,
            description = "Clouds",
            temperature = 293.15,
            humidity = 72,
            windSpeed = 5.0,
            // 2024-10-02 21:35:41 UTC
            date = 1727904941000L,
            iconId = "04d"
        )

        val EXPECTED_SCREEN_ENTITY = CityWeatherDetailsScreenEntity(
            cityName = "Kyiv",
            description = "Clouds",
            temperature = "20.0",
            humidity = "72",
            windSpeed = "18.0",
            date = "02.10.2024 - 21:35",
            iconUrl = "https://openweathermap.org/img/w/04d.png"
        )
    }
}
