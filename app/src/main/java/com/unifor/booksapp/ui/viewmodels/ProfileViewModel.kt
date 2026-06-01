package com.unifor.booksapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.unifor.booksapp.UniforBooksApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ProfileUiState(
    val nome: String = "",
    val matricula: String = "",
    val email: String = "",
    val role: String = "",
    val membroDesde: String = ""
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = (application as UniforBooksApp).sessionManager
    private val apiService = (application as UniforBooksApp).let {
        com.unifor.booksapp.data.remote.RetrofitClient.create(it.sessionManager)
    }

    private val _uiState = MutableStateFlow(buildStateFromSession())
    val uiState = _uiState.asStateFlow()

    init {
        // Se o criadoEm não está na sessão (usuário logado antes da implementação),
        // busca da API e atualiza
        if (sessionManager.getUserCriadoEm().isNullOrBlank()) {
            fetchPerfilFromApi()
        }
    }

    private fun buildStateFromSession(): ProfileUiState {
        val role = sessionManager.getUserRole() ?: "ALUNO"
        val roleLabel = if (role == "ADMIN") "Administrador" else "Aluno"
        return ProfileUiState(
            nome = sessionManager.getUserName() ?: "",
            matricula = sessionManager.getUserMatricula() ?: "",
            email = sessionManager.getUserEmail() ?: "",
            role = roleLabel,
            membroDesde = formatarMembroDesde(sessionManager.getUserCriadoEm())
        )
    }

    private fun fetchPerfilFromApi() {
        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId() ?: return@launch
                val response = apiService.getUserById(userId)
                if (response.isSuccessful) {
                    val usuario = response.body()?.user ?: return@launch
                    val criadoEm = usuario.criadoEm ?: return@launch

                    // Persiste na sessão para chamadas futuras
                    sessionManager.saveSession(
                        accessToken = sessionManager.getAccessToken() ?: "",
                        refreshToken = sessionManager.getRefreshToken() ?: "",
                        userId = sessionManager.getUserId() ?: "",
                        userName = usuario.nome,
                        userEmail = usuario.email,
                        userMatricula = usuario.matricula,
                        userRole = usuario.role.name,
                        userCriadoEm = criadoEm
                    )

                    // Atualiza a UI
                    val role = usuario.role.name
                    val roleLabel = if (role == "ADMIN") "Administrador" else "Aluno"
                    _uiState.value = _uiState.value.copy(
                        nome = usuario.nome,
                        email = usuario.email,
                        matricula = usuario.matricula,
                        role = roleLabel,
                        membroDesde = formatarMembroDesde(criadoEm)
                    )
                }
            } catch (_: Exception) {
                // Falha silenciosa — mantém os dados já exibidos da sessão
            }
        }
    }

    private fun formatarMembroDesde(criadoEm: String?): String {
        if (criadoEm.isNullOrBlank()) return ""
        return try {
            val instant = Instant.parse(criadoEm)
            val formatter = DateTimeFormatter
                .ofPattern("MMMM yyyy", Locale("pt", "BR"))
                .withZone(ZoneId.systemDefault())
            val formatted = formatter.format(instant)
            formatted.replaceFirstChar { it.uppercaseChar() }
        } catch (_: Exception) {
            criadoEm
        }
    }
}
