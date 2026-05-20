package com.unifor.booksapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1Representa os estados da requisição "Login" (autenticação)
sealed class AuthState {
    object Empty : AuthState()                   // Estado inicial (vazio)
    object Loading : AuthState()                // Enquanto a requisição acontece
    object Success : AuthState()                // Login deu certo
    data class Error(val message: String) : AuthState() // Deu erro (senha errada, sem internet)
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Empty)
    val authState: StateFlow<AuthState> = _authState

    // Função principal LOGIN
    fun login(matricula: String, senha: String) {
        // Validação básica
        if (matricula.isBlank() || senha.isBlank()) {
            _authState.value = AuthState.Error("Preencha todos os campos")
            return
        }

        // Muda a tela para "Carregando"
        _authState.value = AuthState.Loading

        // Inicia a requisição assíncrona
        viewModelScope.launch {
            try {
                // Chama repository
                authRepository.login(matricula, senha)
                // Sucesso, caso sem error
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erro: ${e.message}")
            }
        }
    }

    // A Factory que criamos para a MainActivity saber como instanciar
    companion object {
        fun provideFactory(authRepository: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(authRepository) as T
                }
            }
    }
}