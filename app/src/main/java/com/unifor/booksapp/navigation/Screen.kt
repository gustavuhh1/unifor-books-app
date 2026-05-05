package com.unifor.booksapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: String) = "details/$bookId"
    }
}
