package com.yehorlevchenko.presentation.ui.screen.cities

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yehorlevchenko.data.utils.errors.DbError
import com.yehorlevchenko.domain.utils.types.DataSourceType
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.databinding.FragmentCitiesBinding
import com.yehorlevchenko.presentation.di.PresentationAppComponentProvider
import com.yehorlevchenko.presentation.entity.CityScreenEntity
import com.yehorlevchenko.presentation.ui.screen.ScreenDataState
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.UserEvent
import com.yehorlevchenko.presentation.ui.screen.cities.CitiesEvent.Event
import com.yehorlevchenko.presentation.utils.makeGone
import com.yehorlevchenko.presentation.utils.makeVisible
import com.yehorlevchenko.presentation.utils.repeatOnStateResumed
import com.yehorlevchenko.presentation.utils.setSwipeToDelete
import kotlinx.coroutines.flow.collectLatest

class CitiesFragment : Fragment() {

    private lateinit var binding: FragmentCitiesBinding
    private val adapter = CitiesAdapter()

    private val viewModel: CitiesViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as PresentationAppComponentProvider)
            .getPresentationAppComponent()
            .injectCitiesViewModel(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitiesBinding.inflate(layoutInflater)
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
        viewModel.onUserEvent(UserEvent.OnScreenOpen)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
    }

    private fun setViews() {
        binding.rvCitiesList.adapter = adapter
        binding.rvCitiesList.setSwipeToDelete {
            val cityId = adapter.getItemAtPosition(it).id
            viewModel.onUserEvent(UserEvent.OnSwipeToDeleteCity(cityId))
        }
    }

    private fun setListeners() {
        binding.bAddCity.setOnClickListener {
            viewModel.onUserEvent(UserEvent.OnAddCityClick)
        }
        binding.vAddCity.doOnConfirmClick {
            viewModel.onUserEvent(UserEvent.OnAddCityConfirmClick(it))
        }
        adapter.doOnCityItemClick {
            viewModel.onUserEvent(UserEvent.OnCityItemClick(it))
        }
        adapter.doOnCityWeatherHistoryIconClick {
            viewModel.onUserEvent(UserEvent.OnCityWeatherHistoryIconClick(it))
        }
    }

    private fun setObservers() {
        repeatOnStateResumed { viewModel.uiState.collectLatest { renderUIState(it) } }
    }

    private fun renderUIState(uiState: CitiesViewModel.UIState) {
        uiState.event?.let { performEvent(it) }
        setDataState(uiState.dataState)
    }

    private fun setDataState(dataState: ScreenDataState<List<CityScreenEntity>>) {
        when (dataState) {
            is ScreenDataState.Empty -> renderEmptyState()
            is ScreenDataState.Loading -> renderLoadingState()
            is ScreenDataState.Success -> renderSuccessState(dataState.data)
            is ScreenDataState.Error -> renderErrorState(dataState.throwable)
        }
    }

    private fun performEvent(event: Event) {
        when (event) {
            is Event.ShowAddCityView ->
                showAddCityView()
            is Event.HideAddCityView ->
                hideAddCityView()
            is Event.NavigateToCityWeatherHistory ->
                navigateToCityWeatherHistory(event.city)
            is Event.NavigateToCityWeatherDetails ->
                navigateToCityWeatherDetails(event.dataSourceType, event.city)
        }

        viewModel.onUserEvent(UserEvent.OnEventPerformed(event))
    }

    private fun renderEmptyState() {
        binding.pb.hide()
        binding.bAddCity.makeGone()
    }

    private fun renderLoadingState() {
        binding.pb.show()
        binding.bAddCity.makeGone()
    }

    private fun renderSuccessState(items: List<CityScreenEntity>) {
        binding.pb.hide()
        binding.bAddCity.makeVisible()
        adapter.setItems(items)
    }

    private fun renderErrorState(throwable: Throwable?) {
        binding.pb.hide()
        binding.bAddCity.makeVisible()
        throwable?.let {
            when (it) {
                is DbError.NotAvailable ->
                    showToastForStringRes(R.string.error_database_not_available)
                is DbError.InsertError ->
                    showToastForStringRes(R.string.error_database_insert)
                is DbError.DeleteError ->
                    showToastForStringRes(R.string.error_database_delete)
                is DbError.QueryError ->
                    showToastForStringRes(R.string.error_database_query)
                else ->
                    showToastForStringRes(R.string.error_unexpected)
            }
        }
    }

    private fun showAddCityView() = with (binding) {
        bAddCity.makeGone()
        vAddCity.makeVisible()
        showKeyboard()
    }

    private fun hideAddCityView() = with (binding) {
        bAddCity.makeVisible()
        vAddCity.makeGone()
        vAddCity.clear()
        hideKeyboard()
    }

    private fun navigateToCityWeatherHistory(city: CityScreenEntity) {
        findNavController().navigate(
            CitiesFragmentDirections.actionCitiesToCityWeatherHistory(city.id, city.name)
        )
    }

    private fun navigateToCityWeatherDetails(dataSourceType: DataSourceType, city: CityScreenEntity) {
        findNavController().navigate(
            CitiesFragmentDirections.actionCitiesToCityWeatherDetails(dataSourceType, city.name).apply {
                setCityId(city.id)
            }
        )
    }

    private fun showToastForStringRes(stringRes: Int) {
        Toast.makeText(context, getString(stringRes), Toast.LENGTH_LONG).show()
    }

    private val keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        binding.root.getWindowVisibleDisplayFrame(rect)

        val fullHeight = binding.root.height
        val visibleHeight = rect.bottom - rect.top
        val keyboardHeight = fullHeight - visibleHeight

        if (keyboardHeight > 0) onKeyboardShow(keyboardHeight) else onKeyboardHide()
    }

    private fun onKeyboardShow(keyboardHeight: Int) {
        binding.vAddCity.translationY = -keyboardHeight.toFloat()
    }

    private fun onKeyboardHide() {
        binding.vAddCity.translationY = 0F
    }

    private fun showKeyboard() {
        context?.let {
            binding.vAddCity.getEditText().requestFocus()
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.vAddCity.getEditText(), InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        context?.let {
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.vAddCity.getEditText().windowToken, 0)
        }
    }
}