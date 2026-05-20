package com.unifor.booksapp

import android.app.Application
import com.unifor.booksapp.data.AuthRepository
import com.unifor.booksapp.data.BookRepository
import com.unifor.booksapp.data.remote.RetrofitClient
import com.unifor.booksapp.data.session.SessionManager

class UniforBooksApp : Application() {

    val sessionManager by lazy { SessionManager.getInstance(this) }

    private val apiService by lazy { RetrofitClient.create(sessionManager) }

    val authRepository by lazy { AuthRepository(apiService, sessionManager) }

    val bookRepository by lazy { BookRepository(apiService) }
}
