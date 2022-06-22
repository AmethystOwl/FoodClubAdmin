package com.example.foodyadminpanel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditInfoViewModel : ViewModel() {
    private val repo = Repository()
    private var _updateState = MutableLiveData<DataState<Int>>()
    val updateState: LiveData<DataState<Int>> get() = _updateState

    private var _removePreviewImageState = MutableLiveData<DataState<Int>>()
    val removePreviewImageState: LiveData<DataState<Int>> get() = _removePreviewImageState
    fun updateInfo(name: String, loc: String, workingHours: String, imageUri: Uri?, cuisines: ArrayList<String>?, imagesToUpload: List<String>?) {
        viewModelScope.launch {
            repo.updateInfo(name, loc, workingHours, imageUri, cuisines, imagesToUpload).collect {
                _updateState.value = it
            }
        }
    }

    fun removePreviewImage(imageUrl: String) {
        viewModelScope.launch {
            repo.removePreviewImage(imageUrl).collect {
                _removePreviewImageState.value = it
            }
        }
    }

}
