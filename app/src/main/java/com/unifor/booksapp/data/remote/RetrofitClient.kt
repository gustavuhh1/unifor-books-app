package com.unifor.booksapp.data.remote

import com.unifor.booksapp.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://unifor-books-api-core.onrender.com/"

    fun create(sessionManager: SessionManager): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = okhttp3.Interceptor { chain ->
            val token = sessionManager.getAccessToken()
            val request = chain.request().newBuilder().apply {
                addHeader("Accept", "application/json")
                if (token != null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()
            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            // Timeouts agressivos para suportar o cold start do Render.com
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .callTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
