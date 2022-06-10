package com.example.foodyadminpanel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private var _restaurantInfo = MutableLiveData<DataState<Restaurant>>()
     val restaurantInfo : LiveData<DataState<Restaurant>> get() = _restaurantInfo

  private  val repository = Repository()

    fun getRestaurantInfo() {
        viewModelScope.launch {
            repository.getRestaurantInfo().collect {
                _restaurantInfo.value = it
            }
        }
    }

}
