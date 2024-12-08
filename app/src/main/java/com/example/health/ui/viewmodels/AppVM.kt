package com.example.health.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.health.data.apiHelper.Result
import com.example.health.data.remote.models.MedicineResponse
import com.example.health.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppVM @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _appDataState = MutableStateFlow<Result<MedicineResponse>>(Result.Loading())
    val appDataState: StateFlow<Result<MedicineResponse>> = _appDataState.asStateFlow()

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            appRepository.getData().collect {
                _appDataState.value = it
            }
        }
    }
}