package com.unifor.booksapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. Representa os estados possíveis da tela de login
sealed class AuthState {
    object Idle : AuthState()                   // Estado inicial (parado)
    object Loading : AuthState()                // Enquanto a requisição acontece
    object Success : AuthState()                // Login deu certo
    data class Error(val message: String) : AuthState() // Deu erro (senha errada, sem internet)
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // 2. Variável que guarda o estado atual (privada para modificação)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    // Variável pública apenas para leitura (A tela vai observar essa)
    val authState: StateFlow<AuthState> = _authState

    // 3. Função que o botão "Entrar" vai chamar
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
                authRepository.login(matricula, senha)
                // Se não deu erro, atualiza para Sucesso!
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                // Se der erro (API fora, senha incorreta 401, etc)
                _authState.value = AuthState.Error("Erro ao fazer login. Verifique seus dados.")
            }
        }
    }

    // 4. A Factory que criamos para a MainActivity saber como instanciar
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