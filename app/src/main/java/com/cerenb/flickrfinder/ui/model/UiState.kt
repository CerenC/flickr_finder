package com.cerenb.flickrfinder.ui.model

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T>(val data: T) : UiState<T>()

    data class Error<T>(val error: Exception) : UiState<T>()

}