package com.example.foodyadminpanel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddMenuItemViewModel : ViewModel() {
    private val repo = Repository()
    private var _addItemState = MutableLiveData<DataState<Int>>()
     val addItemState: LiveData<DataState<Int>> get() = _addItemState

    fun addMenuItem(item: MenuItem2, selectedCategory: MenuCategory, imageUri: Uri?) {
        viewModelScope.launch {
            repo.addMenuItem(item,selectedCategory,imageUri!!).collect {
                _addItemState.value = it
            }
        }

    }

    fun addMenuItemWithCategory(categoryName: String, item: MenuItem2, imageUri: Uri?) {
        viewModelScope.launch {
            repo.addMenuItemWithCategory(item,imageUri!!,categoryName).collect {
                _addItemState.value = it

            }

        }
    }


}
