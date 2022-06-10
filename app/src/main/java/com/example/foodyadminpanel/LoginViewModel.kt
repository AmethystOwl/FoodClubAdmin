package com.example.foodyadminpanel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var _loginState = MutableLiveData<DataState<Int>>()
     val loginState: LiveData<DataState<Int>> get() = _loginState
    private val repo = Repository()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            repo.login(email, password).collect {
                _loginState.value = it
            }
        }
    }
}
