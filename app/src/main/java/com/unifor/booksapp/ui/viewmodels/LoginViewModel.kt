package com.unifor.booksapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.data.AuthRepository
import com.unifor.booksapp.data.remote.request.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun login() {
        // A lógica de login real foi removida conforme solicitado.
        // A pessoa responsável pela tela de login deverá implementar a chamada à API aqui.
        // Por enquanto, estamos simulando um sucesso para permitir a navegação.
        _uiState.value = LoginUiState.Success
    }
}
