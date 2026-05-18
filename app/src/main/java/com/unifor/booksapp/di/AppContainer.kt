package com.unifor.booksapp.di

import android.content.Context
import com.unifor.booksapp.data.local.TokenManager
import com.unifor.booksapp.data.remote.api.AuthInterceptor
import com.unifor.booksapp.data.remote.api.AuthService
import com.unifor.booksapp.data.repository.AuthRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {
    val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(tokenManager)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://unifor-books-api-core.onrender.com") // TODO: utilizar valor da ".env"
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authService, tokenManager)
    }
}