package com.yehorlevchenko.presentation.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorlevchenko.domain.entity.CityWeatherDetails
import com.yehorlevchenko.domain.usecase.GetActualCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.usecase.GetConvertedIconIdAsIconUrl
import com.yehorlevchenko.domain.usecase.GetFormattedDateAndTime
import com.yehorlevchenko.domain.usecase.GetConvertedKelvinAsCelsius
import com.yehorlevchenko.domain.usecase.GetConvertedMetersPerSecondAsKilometersPerHour
import com.yehorlevchenko.domain.usecase.GetStoredCityWeatherDetailsUseCase
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.entity.CityWeatherDetailsScreenEntity
import com.yehorlevchenko.presentation.mapper.toCityWeatherDetailsScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.Event
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.UserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityWeatherDetailsViewModel: ViewModel() {

    @Inject
    lateinit var getActualCityWeatherDetailsUseCase: GetActualCityWeatherDetailsUseCase

    @Inject
    lateinit var getStoredCityWeatherDetailsUseCase: GetStoredCityWeatherDetailsUseCase

    @Inject
    lateinit var getConvertedKelvinAsCelsius: GetConvertedKelvinAsCelsius

    @Inject
    lateinit var getFormattedDateAndTime: GetFormattedDateAndTime

    @Inject
    lateinit var getConvertedMetersPerSecondAsKilometersPerHour: GetConvertedMetersPerSecondAsKilometersPerHour

    @Inject
    lateinit var getConvertedIconIdAsIconUrl: GetConvertedIconIdAsIconUrl

    data class UIState(
        val event: Event?,
        val data: ScreenDataState<CityWeatherDetailsScreenEntity>
    )

    private val _uiState = MutableStateFlow(createInitialUIState())
    private var events = mutableListOf<Event>()
    val uiState = _uiState.asStateFlow()

    fun onUserEvent(event: UserEvent) = viewModelScope.launch {
        when (event) {
            is UserEvent.OnBackClick ->
                handleBackClick()
            is UserEvent.OnEventPerformed ->
                handleEventPerformed(event.event)
            is UserEvent.OnScreenOpen ->
                handleScreenOpen(event.dataSourceType, event.cityWeatherId, event.cityName, event.cityId)
        }
    }

    private fun handleBackClick() {
        updateUIState(event = Event.NavigateBack)
    }

    private suspend fun handleScreenOpen(
        dataSourceType: DataSourceType,
        cityWeatherId: Long,
        cityName: String,
        cityId: Long
    ) {
        when (dataSourceType) {
            DataSourceType.LOCAL ->
                loadStoredCityWeatherDetails(cityName, cityWeatherId)
            DataSourceType.REMOTE ->
                loadActualCityWeatherDetails(cityName, cityId)
        }
    }

    private suspend fun loadStoredCityWeatherDetails(cityName: String, cityWeatherId: Long) {
        updateUIState(data = ScreenDataState.Loading)
        val result = getStoredCityWeatherDetailsUseCase(cityWeatherId)
        if (result.isSuccess) {
            val data = mapToCityWeatherDetailsScreenEntity(cityName, result.getOrThrow())
            updateUIState(data = ScreenDataState.Success(data))
        } else {
            updateUIState(data = ScreenDataState.Error(result.exceptionOrNull()))
        }
    }

    private suspend fun loadActualCityWeatherDetails(cityName: String, cityId: Long) {
        updateUIState(data = ScreenDataState.Loading)
        val result = getActualCityWeatherDetailsUseCase(cityName, cityId)
        if (result.isSuccess) {
            val data = mapToCityWeatherDetailsScreenEntity(cityName, result.getOrThrow())
            updateUIState(data = ScreenDataState.Success(data))
        } else {
            updateUIState(data = ScreenDataState.Error(result.exceptionOrNull()))
        }
    }

    private fun handleEventPerformed(event: Event) {
        events = events.apply { remove(event) }
        updateUIState(event = events.firstOrNull())
    }

    private fun updateUIState(
        event: Event? = null,
        data: ScreenDataState<CityWeatherDetailsScreenEntity>? = null
    ) {
        event?.let { events += event }

        _uiState.update { previousState ->
            previousState.copy(
                event = events.firstOrNull(),
                data = data ?: previousState.data
            )
        }
    }

    private fun createInitialUIState() = UIState(
        event = null,
        data = ScreenDataState.Empty
    )

    private fun mapToCityWeatherDetailsScreenEntity(
        cityName: String,
        cityWeatherDetails: CityWeatherDetails
    ) : CityWeatherDetailsScreenEntity {
        val temperature = getConvertedKelvinAsCelsius(cityWeatherDetails.temperature)
        val date = getFormattedDateAndTime(cityWeatherDetails.date)
        val windSpeed = getConvertedMetersPerSecondAsKilometersPerHour(cityWeatherDetails.windSpeed)
        val iconUrl = getConvertedIconIdAsIconUrl(cityWeatherDetails.iconId)

        return cityWeatherDetails.toCityWeatherDetailsScreenEntity(
            cityName,
            temperature,
            windSpeed,
            date,
            iconUrl
        )
    }
}