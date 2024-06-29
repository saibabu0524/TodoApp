package com.saibabui.todoapp.ui.presentation.add

import java.lang.Error

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Failure(val error: String) : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
}