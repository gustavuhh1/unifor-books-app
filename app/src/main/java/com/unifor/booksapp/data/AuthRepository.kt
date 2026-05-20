package com.unifor.booksapp.data

import com.unifor.booksapp.data.remote.ApiService
import com.unifor.booksapp.data.remote.request.LoginRequest
import com.unifor.booksapp.data.remote.response.LoginResponse
import com.unifor.booksapp.data.session.SessionManager
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun login(matricula: String, senha: String): Response<LoginResponse> {
        val response = apiService.login(LoginRequest(matricula, senha))
        if (response.isSuccessful) {
            response.body()?.let { body ->
                sessionManager.saveSession(
                    accessToken = body.accessToken,
                    refreshToken = body.refreshToken,
                    userId = body.usuario.id,
                    userName = body.usuario.nome,
                    userEmail = body.usuario.email,
                    userMatricula = body.usuario.matricula,
                    userRole = body.usuario.role.name,
                    userCriadoEm = body.usuario.criadoEm ?: ""
                )
            }
        }
        return response
    }

    suspend fun logout() {
        val refreshToken = sessionManager.getRefreshToken() ?: return
        runCatching { apiService.logout(mapOf("refreshToken" to refreshToken)) }
        sessionManager.clearSession()
    }

    fun isLoggedIn() = sessionManager.isLoggedIn()
}
