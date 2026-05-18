package com.unifor.booksapp

import android.app.Application
import com.unifor.booksapp.di.AppContainer

class BooksApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}