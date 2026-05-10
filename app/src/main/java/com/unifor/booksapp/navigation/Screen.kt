package com.unifor.booksapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: String) = "details/$bookId"
    }
}
