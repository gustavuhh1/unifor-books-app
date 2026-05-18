package com.unifor.booksapp.data.remote.api

import com.unifor.booksapp.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    // O chain.request() é a requisição HTTP original (ex: GET /books)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestOriginal = chain.request()

        // Pega o token do nosso cofre
        val token = tokenManager.getAccessToken()

        // Se não tiver token, deixa a requisição passar como está (ex: Rota de Login)
        if (token == null) {
            return chain.proceed(requestOriginal)
        }

        // Se tiver token, cria uma cópia da requisição e adiciona o cabeçalho
        val novaRequisicao = requestOriginal.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        // Continua a viagem da requisição até o servidor Node.js
        return chain.proceed(novaRequisicao)
    }
}