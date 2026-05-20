package com.unifor.booksapp.data.repository

import com.unifor.booksapp.data.local.TokenManager
import com.unifor.booksapp.data.remote.api.AuthService
import com.unifor.booksapp.data.remote.api.LoginRequest

class AuthRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {
    suspend fun login(matricula: String, senha: String) {
        val response = authService.login(LoginRequest(matricula, senha))

        tokenManager.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }
}