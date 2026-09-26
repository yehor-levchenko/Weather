package com.yehorlevchenko.presentation.ui.screen.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorlevchenko.domain.entity.City
import com.yehorlevchenko.domain.usecase.DeleteCityUseCase
import com.yehorlevchenko.domain.usecase.GetAllCitiesUseCase
import com.yehorlevchenko.domain.usecase.SaveCityUseCase
import com.yehorlevchenko.domain.utils.types.DataSourceType.REMOTE
import com.yehorlevchenko.presentation.entity.CityScreenEntity
import com.yehorlevchenko.presentation.mapper.toCityScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.UserEvent
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CitiesViewModel : ViewModel() {

    @Inject
    lateinit var saveCityUseCase: SaveCityUseCase

    @Inject
    lateinit var deleteCityUseCase: DeleteCityUseCase

    @Inject
    lateinit var getAllCitiesUseCase: GetAllCitiesUseCase

    data class UIState(
        val event: Event?,
        val dataState: ScreenDataState<List<CityScreenEntity>>
    )

    private val _uiState = MutableStateFlow(createInitialUIState())
    private var events = mutableListOf<Event>()
    val uiState = _uiState.asStateFlow()

    fun onUserEvent(event: UserEvent) = viewModelScope.launch {
        when (event) {
            is UserEvent.OnScreenOpen ->
                handleScreenOpen()
            is UserEvent.OnAddCityClick ->
                handleAddCityClick()
            is UserEvent.OnAddCityConfirmClick ->
                handleAddCityConfirmClick(event.cityName)
            is UserEvent.OnSwipeToDeleteCity ->
                handleSwipeToDeleteCity(event.cityId)
            is UserEvent.OnCityItemClick ->
                handleCityItemClick(event.city)
            is UserEvent.OnCityWeatherHistoryIconClick ->
                handleCityWeatherHistoryIconClick(event.city)
            is UserEvent.OnEventPerformed ->
                handleEventPerformed(event.event)
        }
    }

    private suspend fun handleScreenOpen() {
        loadAllCities()
    }

    private fun handleAddCityClick() {
        updateUIState(event = Event.ShowAddCityView)
    }

    private suspend fun handleAddCityConfirmClick(cityName: String) {
        saveCityUseCase(City(0, cityName))
        updateUIState(event = Event.HideAddCityView)
        loadAllCities()
    }

    private suspend fun handleSwipeToDeleteCity(cityId: Long) {
        deleteCityUseCase(cityId)
        loadAllCities()
    }

    private fun handleCityItemClick(city: CityScreenEntity) {
        updateUIState(event = Event.NavigateToCityWeatherDetails(REMOTE, city))
    }

    private fun handleCityWeatherHistoryIconClick(city: CityScreenEntity) {
        updateUIState(event = Event.NavigateToCityWeatherHistory(city))
    }

    private fun handleEventPerformed(event: Event) {
        events = events.apply { remove(event) }
        updateUIState(event = events.firstOrNull())
    }

    private fun updateUIState(
        event: Event? = null,
        dataState: ScreenDataState<List<CityScreenEntity>>? = null
    ) {
        event?.let { events += event }

        _uiState.update { previousState ->
            previousState.copy(
                event = events.firstOrNull(),
                dataState = dataState ?: previousState.dataState
            )
        }
    }

    private fun createInitialUIState() = UIState(
        event = null,
        dataState = ScreenDataState.Empty
    )

    private suspend fun loadAllCities() {
        updateUIState(dataState = ScreenDataState.Loading)
        val result = getAllCitiesUseCase()
        if (result.isSuccess) {
            val data = result.getOrThrow().map { it.toCityScreenEntity() }
            updateUIState(dataState = ScreenDataState.Success(data))
        } else {
            updateUIState(dataState = ScreenDataState.Error(result.exceptionOrNull()))
        }
    }
}