package com.jgcoding.kotlin.commercelistapp.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import com.jgcoding.kotlin.commercelistapp.domain.usecase.GetCommerceIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getCommerceIdUseCase: GetCommerceIdUseCase): ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private var _uiState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    val uiState: StateFlow<DetailViewState> get() = _uiState

    fun getCommerceId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val commerce = getCommerceIdUseCase(id).first()
            if(commerce != null) {
                Log.d(TAG, "getCommerceId: ${commerce.name}")
                _uiState.value = DetailViewState.Success(commerce)
            }
            else
                _uiState.value = DetailViewState.Error("No se encontro el comercio")
        }

    }
}

sealed class DetailViewState {
    data object Loading : DetailViewState()
    data class Success(val commerce: Commerce) : DetailViewState()
    data class Error(val error: String) : DetailViewState()
}