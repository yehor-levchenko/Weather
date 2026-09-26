package com.yehorlevchenko.presentation.ui.screen.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.yehorlevchenko.data.utils.errors.ApiError
import com.yehorlevchenko.data.utils.errors.DbError
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.databinding.FragmentCityWeatherDetailsBinding
import com.yehorlevchenko.presentation.di.PresentationAppComponentProvider
import com.yehorlevchenko.presentation.entity.CityWeatherDetailsScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.utils.repeatOnStateResumed
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.Event
import com.yehorlevchenko.presentation.ui.screen.details.CityWeatherDetailsEvent.UserEvent
import com.yehorlevchenko.presentation.utils.makeGone
import com.yehorlevchenko.presentation.utils.makeVisible
import kotlinx.coroutines.flow.collectLatest

class CityWeatherDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCityWeatherDetailsBinding

    private val viewModel: CityWeatherDetailsViewModel by viewModels()
    private val args: CityWeatherDetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as PresentationAppComponentProvider)
            .getPresentationAppComponent()
            .injectCityWeatherDetailsViewModel(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUserEvent(UserEvent.OnScreenOpen(args.dataSourceType, args.cityWeatherId, args.cityName, args.cityId))
    }

    private fun setListeners() {
        binding.vToolbar.doOnBackClick {
            viewModel.onUserEvent(UserEvent.OnBackClick)
        }
    }

    private fun setObservers() {
        repeatOnStateResumed { viewModel.uiState.collectLatest { renderUIState(it) } }
    }

    private fun renderUIState(uiState: CityWeatherDetailsViewModel.UIState) = with (binding) {
        uiState.event?.let { performEvent(it) }
        setDataState(uiState.data)
    }

    private fun setDataState(dataState: ScreenDataState<CityWeatherDetailsScreenEntity>) {
        when (dataState) {
            is ScreenDataState.Empty -> renderEmptyState()
            is ScreenDataState.Loading -> renderLoadingState()
            is ScreenDataState.Success -> renderSuccessState(dataState.data)
            is ScreenDataState.Error -> renderErrorState(dataState.throwable)
        }
    }

    private fun performEvent(event: Event) {
        when (event) {
            is Event.NavigateBack ->
                navigateBack()
        }

        viewModel.onUserEvent(UserEvent.OnEventPerformed(event))
    }

    private fun renderEmptyState() {
        binding.pb.hide()
        toggleContentVisibility(false)
    }

    private fun renderLoadingState() {
        binding.pb.show()
        toggleContentVisibility(false)
    }

    private fun renderSuccessState(data: CityWeatherDetailsScreenEntity) {
        binding.pb.hide()
        toggleContentVisibility(true)
        setCity(data.cityName)
        setDescription(data.description)
        setTemperature(data.temperature)
        setHumidity(data.humidity)
        setWindSpeed(data.windSpeed)
        setWeatherReceivingDetails(data.cityName, data.date)
        setIcon(data.iconUrl)
    }

    private fun renderErrorState(throwable: Throwable?) {
        binding.pb.hide()
        toggleContentVisibility(false)
        throwable?.let {
            when (it) {
                is DbError.NotAvailable ->
                    showToastForStringRes(R.string.error_database_not_available)
                is DbError.QueryError ->
                    showToastForStringRes(R.string.error_database_query)
                is ApiError.NetworkError ->
                    showToastForStringRes(R.string.error_api_network_error)
                is ApiError.HttpError ->
                    showToastForStringRes(R.string.error_api_http_error)
                is ApiError.InternalServerError ->
                    showToastForStringRes(R.string.error_api_internal_server_error)
                is ApiError.NotFound ->
                    showToastForStringRes(R.string.error_api_not_found)
                is ApiError.Unauthorized ->
                    showToastForStringRes(R.string.error_api_request_unauthorized)
                else ->
                    showToastForStringRes(R.string.error_unexpected)
            }
        }
    }

    private fun toggleContentVisibility(visible: Boolean) = with (binding) {
        if (visible) tvCityAndCountry.makeVisible() else tvCityAndCountry.makeGone()
        if (visible) ivWeatherDescriptionIcon.makeVisible() else ivWeatherDescriptionIcon.makeGone()
        if (visible) tvTitleDescription.makeVisible() else tvTitleDescription.makeGone()
        if (visible) tvDataDescription.makeVisible() else tvDataDescription.makeGone()
        if (visible) tvTitleTemperature.makeVisible() else tvTitleTemperature.makeGone()
        if (visible) tvDataTemperature.makeVisible() else tvDataTemperature.makeGone()
        if (visible) tvTitleHumidity.makeVisible() else tvTitleHumidity.makeGone()
        if (visible) tvDataHumidity.makeVisible() else tvDataHumidity.makeGone()
        if (visible) tvTitleWindspeed.makeVisible() else tvTitleWindspeed.makeGone()
        if (visible) tvDataWindspeed.makeVisible() else tvDataWindspeed.makeGone()
        if (visible) tvWeatherInfoReceivingDetails.makeVisible() else tvWeatherInfoReceivingDetails.makeGone()
    }

    private fun setCity(cityName: String) {
        binding.tvCityAndCountry.text = cityName
    }

    private fun setDescription(description: String) {
        binding.tvDataDescription.text = description
    }

    private fun setTemperature(temperature: String) {
        val value = getString(R.string.weather_details_data_placeholder_temperature, temperature)
        binding.tvDataTemperature.text = value
    }

    private fun setHumidity(humidity: String) {
        val value = getString(R.string.weather_details_data_placeholder_humidity, humidity)
        binding.tvDataHumidity.text = value
    }

    private fun setWindSpeed(windSpeed: String) {
        val value = getString(R.string.weather_details_data_placeholder_windspeed, windSpeed)
        binding.tvDataWindspeed.text = value
    }

    private fun setWeatherReceivingDetails(cityName: String, date: String) {
        val value = getString(R.string.placeholder_weather_details_receiving_details, cityName, date)
        binding.tvWeatherInfoReceivingDetails.text = value
    }

    private fun setIcon(iconUrl: String) {
        Picasso.get().load(iconUrl).error(R.drawable.ic_weather_description_icon_placeholder).into(binding.ivWeatherDescriptionIcon)
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun showToastForStringRes(stringRes: Int) {
        Toast.makeText(context, getString(stringRes), Toast.LENGTH_LONG).show()
    }
}