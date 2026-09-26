package com.yehorlevchenko.presentation.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorlevchenko.domain.entity.CityWeatherHistory
import com.yehorlevchenko.domain.usecase.GetCityWeatherHistoryUseCase
import com.yehorlevchenko.domain.usecase.GetConvertedIconIdAsIconUrl
import com.yehorlevchenko.domain.usecase.GetConvertedKelvinAsCelsius
import com.yehorlevchenko.domain.usecase.GetFormattedDateAndTime
import com.yehorlevchenko.domain.utils.types.DataSourceType.LOCAL
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.UserEvent
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.Event
import com.yehorlevchenko.presentation.entity.CityWeatherHistoryScreenEntity
import com.yehorlevchenko.presentation.mapper.toCityWeatherHistoryScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityWeatherHistoryViewModel : ViewModel() {

    @Inject
    lateinit var getCityWeatherHistoryUseCase: GetCityWeatherHistoryUseCase

    @Inject
    lateinit var getFormattedDateAndTime: GetFormattedDateAndTime

    @Inject
    lateinit var getConvertedKelvinAsCelsius: GetConvertedKelvinAsCelsius

    @Inject
    lateinit var getConvertedIconIdAsIconUrl: GetConvertedIconIdAsIconUrl

    data class UIState(
        val event: Event?,
        val dataState: ScreenDataState<List<CityWeatherHistoryScreenEntity>>,
        val cityName: String
    )

    private val _uiState = MutableStateFlow(createInitialUIState())
    private var events = mutableListOf<Event>()
    val uiState = _uiState.asStateFlow()

    fun onUserEvent(event: UserEvent) = viewModelScope.launch {
        when (event) {
            is UserEvent.OnBackClick ->
                handleBackClick()
            is UserEvent.OnScreenOpen ->
                handleScreenOpen(event.cityId, event.cityName)
            is UserEvent.OnCityWeatherHistoryItemClick ->
                handleCityWeatherHistoryItemClick(event.cityWeatherId)
            is UserEvent.OnEventPerformed ->
                handleEventPerformed(event.event)
        }
    }

    private fun handleBackClick() {
        updateUIState(event = Event.NavigateBack)
    }

    private suspend fun handleScreenOpen(cityId: Long, cityName: String) {
        updateUIState(cityName = cityName, dataState = ScreenDataState.Loading)
        val result = getCityWeatherHistoryUseCase(cityId)
        if (result.isSuccess) {
            val data = result.getOrThrow().map { mapToCityWeatherHistoryScreenEntity(it) }
            updateUIState(dataState = ScreenDataState.Success(data))
        } else {
            updateUIState(dataState = ScreenDataState.Error(result.exceptionOrNull()))
        }
    }

    private fun handleCityWeatherHistoryItemClick(cityWeatherId: Long) {
        updateUIState(event = Event.NavigateToCityWeatherDetails(LOCAL, cityWeatherId, getUIState().cityName))
    }

    private fun handleEventPerformed(event: Event) {
        events = events.apply { remove(event) }
        updateUIState(event = events.firstOrNull())
    }

    private fun getUIState(): UIState = _uiState.value

    private fun updateUIState(
        event: Event? = null,
        dataState: ScreenDataState<List<CityWeatherHistoryScreenEntity>>? = null,
        cityName: String? = null
    ) {
        event?.let { events += event }

        _uiState.update { previousState ->
            previousState.copy(
                event = events.firstOrNull(),
                dataState = dataState ?: previousState.dataState,
                cityName = cityName ?: previousState.cityName
            )
        }
    }

    private fun createInitialUIState() = UIState(
        event = null,
        dataState = ScreenDataState.Empty,
        cityName = ""
    )

    private fun mapToCityWeatherHistoryScreenEntity(
        cityWeatherHistory: CityWeatherHistory
    ) : CityWeatherHistoryScreenEntity {
        val temperature = getConvertedKelvinAsCelsius(cityWeatherHistory.temperature)
        val date = getFormattedDateAndTime(cityWeatherHistory.date)
        val iconUrl = getConvertedIconIdAsIconUrl(cityWeatherHistory.iconId)

        return cityWeatherHistory.toCityWeatherHistoryScreenEntity(
            temperature = temperature,
            date = date,
            iconUrl = iconUrl
        )
    }
}