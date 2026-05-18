package com.unifor.booksapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val matricula: String, val senha: String)
data class LoginResponse(val accessToken: String, val refreshToken: String)

interface AuthService {
    // Endpoint do Fastify para login
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}