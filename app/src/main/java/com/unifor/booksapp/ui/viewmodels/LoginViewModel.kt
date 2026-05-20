package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = (application as UniforBooksApp).authRepository

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    val matricula = MutableStateFlow("")
    val senha = MutableStateFlow("")

    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(matricula.value.trim(), senha.value)
                if (response.isSuccessful) {
                    _uiState.value = LoginUiState.Success
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Matrícula ou senha incorretos"
                        else -> "Erro ao fazer login (${response.code()})"
                    }
                    _uiState.value = LoginUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Erro de conexão")
            }
        }
    }
}
