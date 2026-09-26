package com.yehorlevchenko.presentation.ui.screen.cities

import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.usecase.DeleteCityUseCase
import com.yehorlevchenko.domain.usecase.GetAllCitiesUseCase
import com.yehorlevchenko.domain.usecase.SaveCityUseCase
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.entity.CityScreenEntity
import com.yehorlevchenko.presentation.fake.FakeCityRepository
import com.yehorlevchenko.presentation.rule.MainDispatcherRule
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.Event
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.UserEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CitiesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCityRepository()
    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setUp() {
        viewModel = CitiesViewModel().apply {
            saveCityUseCase = SaveCityUseCase(repository)
            deleteCityUseCase = DeleteCityUseCase(repository)
            getAllCitiesUseCase = GetAllCitiesUseCase(repository)
        }
    }

    @Test
    fun `initial state is empty without events`() {
        assertNull(viewModel.uiState.value.event)
        assertEquals(ScreenDataState.Empty, viewModel.uiState.value.dataState)
    }

    @Test
    fun `stored cities are shown on screen open`() {
        repository.cities += City(id = 1, name = "Kyiv")

        viewModel.onUserEvent(UserEvent.OnScreenOpen)

        assertEquals(
            ScreenDataState.Success(listOf(CityScreenEntity(id = 1, name = "Kyiv"))),
            viewModel.uiState.value.dataState
        )
    }

    @Test
    fun `error is shown when cities cannot be loaded`() {
        val error = RuntimeException()
        repository.error = error

        viewModel.onUserEvent(UserEvent.OnScreenOpen)

        assertEquals(ScreenDataState.Error(error), viewModel.uiState.value.dataState)
    }

    @Test
    fun `add city view is shown on add city click`() {
        viewModel.onUserEvent(UserEvent.OnAddCityClick)

        assertEquals(Event.ShowAddCityView, viewModel.uiState.value.event)
    }

    @Test
    fun `confirmed city is saved and add city view is hidden`() {
        viewModel.onUserEvent(UserEvent.OnAddCityConfirmClick("Lviv"))

        assertEquals(Event.HideAddCityView, viewModel.uiState.value.event)
        assertEquals(
            ScreenDataState.Success(listOf(CityScreenEntity(id = 1, name = "Lviv"))),
            viewModel.uiState.value.dataState
        )
    }

    @Test
    fun `swiped city is deleted`() {
        repository.cities += listOf(City(id = 1, name = "Kyiv"), City(id = 2, name = "Lviv"))

        viewModel.onUserEvent(UserEvent.OnSwipeToDeleteCity(cityId = 1))

        assertEquals(
            ScreenDataState.Success(listOf(CityScreenEntity(id = 2, name = "Lviv"))),
            viewModel.uiState.value.dataState
        )
    }

    @Test
    fun `city click navigates to actual weather details`() {
        val city = CityScreenEntity(id = 1, name = "Kyiv")

        viewModel.onUserEvent(UserEvent.OnCityItemClick(city))

        val event = viewModel.uiState.value.event
        assertTrue(event is Event.NavigateToCityWeatherDetails)
        event as Event.NavigateToCityWeatherDetails
        assertEquals(DataSourceType.REMOTE, event.dataSourceType)
        assertEquals(city, event.city)
    }

    @Test
    fun `history icon click navigates to weather history`() {
        val city = CityScreenEntity(id = 1, name = "Kyiv")

        viewModel.onUserEvent(UserEvent.OnCityWeatherHistoryIconClick(city))

        val event = viewModel.uiState.value.event
        assertTrue(event is Event.NavigateToCityWeatherHistory)
        assertEquals(city, (event as Event.NavigateToCityWeatherHistory).city)
    }

    @Test
    fun `performed event is removed from state`() {
        viewModel.onUserEvent(UserEvent.OnAddCityClick)

        viewModel.onUserEvent(UserEvent.OnEventPerformed(Event.ShowAddCityView))

        assertNull(viewModel.uiState.value.event)
    }

    @Test
    fun `events are delivered one by one in order`() {
        viewModel.onUserEvent(UserEvent.OnAddCityClick)
        viewModel.onUserEvent(UserEvent.OnAddCityConfirmClick("Lviv"))

        assertEquals(Event.ShowAddCityView, viewModel.uiState.value.event)

        viewModel.onUserEvent(UserEvent.OnEventPerformed(Event.ShowAddCityView))

        assertEquals(Event.HideAddCityView, viewModel.uiState.value.event)
    }
}
