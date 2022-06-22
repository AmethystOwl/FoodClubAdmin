package com.example.foodyadminpanel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MenuItemViewModel : ViewModel() {
    private val repo = Repository()
    private var _updateState = MutableLiveData<DataState<Int>>()
    val updateState: LiveData<DataState<Int>> get() = _updateState
    fun updateMenuItem(newItem: MenuItem2, imageUri: Uri?) {
        viewModelScope.launch {
            repo.updateMenuItem(newItem, imageUri).collect {
                _updateState.value = it
            }
        }

    }

}
