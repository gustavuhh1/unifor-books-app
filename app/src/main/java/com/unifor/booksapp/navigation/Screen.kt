package com.unifor.booksapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: String) = "details/$bookId"
    }

    // Telas de status de empréstimo
    object LoanApproved : Screen("loan/approved")
    object LoanUnavailable : Screen("loan/unavailable")
    object LoanQueue : Screen("loan/queue/{position}") {
        fun createRoute(position: Int) = "loan/queue/$position"
    }
}
