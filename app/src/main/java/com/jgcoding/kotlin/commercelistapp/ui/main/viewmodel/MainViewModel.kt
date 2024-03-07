package com.jgcoding.kotlin.commercelistapp.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import com.jgcoding.kotlin.commercelistapp.domain.usecase.GetCommercesDatabaseUseCase
import com.jgcoding.kotlin.commercelistapp.domain.usecase.GetCommercesUseCase
import com.jgcoding.kotlin.commercelistapp.domain.usecase.SaveCommerceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCommercesUseCase: GetCommercesUseCase,
    private val saveCommerceUseCase: SaveCommerceUseCase,
    private val getCommercesDatabaseUseCase: GetCommercesDatabaseUseCase
) :
    ViewModel() {

    private var _uiState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val uiState: StateFlow<MainViewState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = MainViewState.Loading
            val result = withContext(Dispatchers.IO) {
                getCommercesUseCase()
            }
            if (result.isNullOrEmpty()) {
//                _uiState.value = MainViewState.Error("No hay comercios")
                withContext(Dispatchers.IO) {
                    getCommercesDatabaseUseCase().collect{
                        if(it.isNotEmpty())
                            _uiState.value = MainViewState.Success(it)
                        else
                            _uiState.value = MainViewState.Error("No hay comercios en la base de datos")
                    }
                }
            }else {
                _uiState.value = MainViewState.Success(result)
                //Se podría hacer un delete antes cada x tiempo para limpiar las entradas antiguas y que ya no estén en la lista
                // o buscar los objetos que estan en la base de datos y no en la lista para actualizar los que ya están y borrar las que no
                saveCommerceUseCase(result)
            }
        }
    }

}

sealed class MainViewState {
    data object Loading : MainViewState()
    data class Success(val commerces: List<Commerce>) : MainViewState()
    data class Error(val error: String) : MainViewState()
}