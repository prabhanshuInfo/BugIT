package com.mobily.bugit.utils

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failed(val errorMessage: String) : Result<Nothing>()
}