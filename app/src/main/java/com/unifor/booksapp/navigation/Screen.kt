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

    // Telas de denúncia de comentário
    object ReportComment : Screen("report/{commentId}") {
        fun createRoute(commentId: String) = "report/$commentId"
    }
    object ReportConfirmation : Screen("report/confirmation")

    // Telas de Meus Empréstimos
    object MyLoans : Screen("loans")
    object FinesPolicy : Screen("fines")
    object RenewalAvailable : Screen("renewal/available")
    object RenewalUnavailable : Screen("renewal/unavailable")
}
