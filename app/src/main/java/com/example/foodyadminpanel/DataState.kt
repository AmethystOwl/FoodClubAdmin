package com.example.foodyadminpanel

sealed class DataState<out R> {

    data class Success<out T>(val data: T) : DataState<T>()
    data class Progress<out T>(val data: T) : DataState<T>()

    data class Error(val exception: Exception) : DataState<Nothing>()
    data class Invalid<out T>(val data: T) : DataState<T>()

}
