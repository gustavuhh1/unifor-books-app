package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.unifor.booksapp.UniforBooksApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val nome: String = "",
    val matricula: String = "",
    val email: String = "",
    val role: String = "",
    val criadoEm: String = ""
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = (application as UniforBooksApp).sessionManager

    private val _uiState = MutableStateFlow(buildState())
    val uiState = _uiState.asStateFlow()

    private fun buildState(): ProfileUiState {
        val role = sessionManager.getUserRole() ?: "ALUNO"
        val roleLabel = if (role == "ADMIN") "Administrador" else "Aluno"
        return ProfileUiState(
            nome = sessionManager.getUserName() ?: "",
            matricula = sessionManager.getUserMatricula() ?: "",
            email = sessionManager.getUserEmail() ?: "",
            role = roleLabel,
            criadoEm = ""
        )
    }
}
