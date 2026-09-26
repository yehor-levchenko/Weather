package com.yehorlevchenko.presentation.ui.screen

sealed class ScreenDataState<out T> {
    data object Empty : ScreenDataState<Nothing>()
    data object Loading : ScreenDataState<Nothing>()
    data class Success<T>(val data: T) : ScreenDataState<T>()
    data class Error(val throwable: Throwable?) : ScreenDataState<Nothing>()
}