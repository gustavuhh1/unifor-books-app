package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
        if (matricula.value.isBlank() || senha.value.isBlank()) {
            _uiState.value = LoginUiState.Error("Preencha todos os campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(matricula.value.trim(), senha.value)
                if (response.isSuccessful) {
                    _uiState.value = LoginUiState.Success
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Matrícula ou senha incorretos"
                        404 -> "Serviço não encontrado (404)"
                        500 -> "Erro interno no servidor (500)"
                        503 -> "Servidor em manutenção ou acordando (503)"
                        else -> "Erro ${response.code()}: ${response.message()}"
                    }
                    _uiState.value = LoginUiState.Error(errorMsg)
                }
            } catch (e: SocketTimeoutException) {
                Log.e("LoginViewModel", "Timeout", e)
                _uiState.value = LoginUiState.Error("O servidor demorou muito para responder. Tente novamente em instantes.")
            } catch (e: UnknownHostException) {
                Log.e("LoginViewModel", "DNS Error", e)
                _uiState.value = LoginUiState.Error("Não foi possível localizar o servidor. Verifique sua internet.")
            } catch (e: ConnectException) {
                Log.e("LoginViewModel", "Connect Error", e)
                _uiState.value = LoginUiState.Error("Falha ao conectar ao servidor. O serviço pode estar offline.")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Generic Error", e)
                _uiState.value = LoginUiState.Error("Erro inesperado: ${e.localizedMessage ?: "Conexão falhou"}")
            }
        }
    }
}
