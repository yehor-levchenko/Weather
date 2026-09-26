package com.yehorlevchenko.presentation.ui.screen.history

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
import com.yehorlevchenko.data.utils.errors.DbError
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.databinding.FragmentCityWeatherHistoryBinding
import com.yehorlevchenko.presentation.di.PresentationAppComponentProvider
import com.yehorlevchenko.presentation.entity.CityWeatherHistoryScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.utils.repeatOnStateResumed
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.UserEvent
import com.yehorlevchenko.presentation.ui.screen.history.CityWeatherHistoryEvent.Event
import kotlinx.coroutines.flow.collectLatest

class CityWeatherHistoryFragment : Fragment() {

    private lateinit var binding: FragmentCityWeatherHistoryBinding
    private val adapter = CityWeatherHistoryAdapter()

    private val viewModel: CityWeatherHistoryViewModel by viewModels()
    private val args: CityWeatherHistoryFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as PresentationAppComponentProvider)
            .getPresentationAppComponent()
            .injectCityWeatherHistoryViewModel(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setListeners()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUserEvent(UserEvent.OnScreenOpen(args.cityId, args.cityName))
    }

    private fun setViews() {
        binding.rvWeatherHistory.adapter = adapter
    }

    private fun setListeners() {
        binding.vToolbar.doOnBackClick {
            viewModel.onUserEvent(UserEvent.OnBackClick)
        }
        adapter.doOnCityWeatherHistoryItemClick {
            viewModel.onUserEvent(UserEvent.OnCityWeatherHistoryItemClick(it))
        }
    }

    private fun setObservers() {
        repeatOnStateResumed { viewModel.uiState.collectLatest { renderUIState(it) } }
    }

    private fun renderUIState(uiState: CityWeatherHistoryViewModel.UIState) {
        uiState.event?.let { performEvent(it) }
        setToolbarTitle(uiState.cityName)
        setDataState(uiState.dataState)
    }

    private fun setDataState(dataState: ScreenDataState<List<CityWeatherHistoryScreenEntity>>) {
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
            is Event.NavigateToCityWeatherDetails ->
                navigateToCityWeatherDetails(event.dataSourceType, event.cityWeatherId, event.cityName)
        }

        viewModel.onUserEvent(UserEvent.OnEventPerformed(event))
    }

    private fun renderEmptyState() {
        binding.pb.hide()
    }

    private fun renderLoadingState() {
        binding.pb.show()
    }

    private fun renderSuccessState(items: List<CityWeatherHistoryScreenEntity>) {
        binding.pb.hide()
        adapter.setItems(items)
    }

    private fun renderErrorState(throwable: Throwable?) {
        binding.pb.hide()
        throwable?.let {
            when (it) {
                is DbError.NotAvailable ->
                    showToastForStringRes(R.string.error_database_not_available)
                is DbError.QueryError ->
                    showToastForStringRes(R.string.error_database_query)
                else ->
                    showToastForStringRes(R.string.error_unexpected)
            }
        }
    }

    private fun setToolbarTitle(cityName: String) {
        val value = getString(R.string.toolbar_title_city_historical, cityName)
        binding.vToolbar.setTitle(value)
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun navigateToCityWeatherDetails(
        dataSourceType: DataSourceType,
        cityWeatherId: Long,
        cityName: String
    ) {
        findNavController().navigate(
            CityWeatherHistoryFragmentDirections
                .actionHistoryToCityWeatherDetails(dataSourceType, cityName)
                .apply { setCityWeatherId(cityWeatherId) }
        )
    }

    private fun showToastForStringRes(stringRes: Int) {
        Toast.makeText(context, getString(stringRes), Toast.LENGTH_LONG).show()
    }
}