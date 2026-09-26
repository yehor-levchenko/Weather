package com.yehorlevchenko.presentation.ui.screen.history

import com.yehorlevchenko.domain.entity.CityWeatherHistory
import com.yehorlevchenko.domain.usecase.GetCityWeatherHistoryUseCase
import com.yehorlevchenko.domain.usecase.GetConvertedIconIdAsIconUrl
import com.yehorlevchenko.domain.usecase.GetConvertedKelvinAsCelsius
import com.yehorlevchenko.domain.usecase.GetFormattedDateAndTime
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.entity.CityWeatherHistoryScreenEntity
import com.yehorlevchenko.presentation.fake.FakeCityWeatherHistoryRepository
import com.yehorlevchenko.presentation.rule.MainDispatcherRule
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.Event
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.UserEvent
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import java.util.TimeZone

class CityWeatherHistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCityWeatherHistoryRepository()
    private lateinit var viewModel: CityWeatherHistoryViewModel
    private lateinit var defaultTimeZone: TimeZone
    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultTimeZone = TimeZone.getDefault()
        defaultLocale = Locale.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Locale.setDefault(Locale.US)

        viewModel = CityWeatherHistoryViewModel().apply {
            getCityWeatherHistoryUseCase = GetCityWeatherHistoryUseCase(repository)
            getFormattedDateAndTime = GetFormattedDateAndTime()
            getConvertedKelvinAsCelsius = GetConvertedKelvinAsCelsius()
            getConvertedIconIdAsIconUrl = GetConvertedIconIdAsIconUrl()
        }
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(defaultTimeZone)
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `history of the city is loaded and converted on screen open`() {
        repository.history[7] = listOf(
            CityWeatherHistory(id = 1, description = "Rain", temperature = 283.15, date = 0L, iconId = "10d")
        )

        viewModel.onUserEvent(UserEvent.OnScreenOpen(cityId = 7, cityName = "Kyiv"))

        assertEquals("Kyiv", viewModel.uiState.value.cityName)
        assertEquals(
            ScreenDataState.Success(
                listOf(
                    CityWeatherHistoryScreenEntity(
                        id = 1,
                        description = "Rain",
                        temperature = "10.0",
                        date = "01.01.1970 - 00:00",
                        iconUrl = "https://openweathermap.org/img/w/10d.png"
                    )
                )
            ),
            viewModel.uiState.value.dataState
        )
    }

    @Test
    fun `error is shown when history cannot be loaded`() {
        val error = RuntimeException()
        repository.error = error

        viewModel.onUserEvent(UserEvent.OnScreenOpen(cityId = 7, cityName = "Kyiv"))

        assertEquals(ScreenDataState.Error(error), viewModel.uiState.value.dataState)
    }

    @Test
    fun `history item click navigates to stored weather details of the city`() {
        viewModel.onUserEvent(UserEvent.OnScreenOpen(cityId = 7, cityName = "Kyiv"))

        viewModel.onUserEvent(UserEvent.OnCityWeatherHistoryItemClick(cityWeatherId = 1))

        val event = viewModel.uiState.value.event
        assertTrue(event is Event.NavigateToCityWeatherDetails)
        event as Event.NavigateToCityWeatherDetails
        assertEquals(DataSourceType.LOCAL, event.dataSourceType)
        assertEquals(1, event.cityWeatherId)
        assertEquals("Kyiv", event.cityName)
    }

    @Test
    fun `back click navigates back`() {
        viewModel.onUserEvent(UserEvent.OnBackClick)

        assertEquals(Event.NavigateBack, viewModel.uiState.value.event)
    }
}
