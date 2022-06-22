package com.example.foodyadminpanel

sealed class DataState<out R> {
    object Canceled : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Progress<out T>(val data: T) : DataState<T>()

    data class Error(val exception: Exception) : DataState<Nothing>()
    data class Invalid<out T>(val data: T) : DataState<T>()

}
