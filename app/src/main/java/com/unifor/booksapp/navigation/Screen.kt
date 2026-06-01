package com.unifor.booksapp.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: String) = "details/$bookId"
    }
    object Profile : Screen("profile")

    // Alterar Senha
    object ChangePassword : Screen("profile/change-password")
    object ChangePasswordSuccess : Screen("profile/change-password/success")

    // Telas de status de empréstimo
    object LoanApproved : Screen("loan/approved?title={title}&author={author}&deadline={deadline}") {
        fun createRoute(title: String, author: String, deadline: String = "") =
            "loan/approved?title=${Uri.encode(title)}&author=${Uri.encode(author)}&deadline=${Uri.encode(deadline)}"
    }
    object LoanUnavailable : Screen("loan/unavailable?title={title}&author={author}") {
        fun createRoute(title: String, author: String) =
            "loan/unavailable?title=${Uri.encode(title)}&author=${Uri.encode(author)}"
    }
    object LoanQueue : Screen("loan/queue/{position}?title={title}&author={author}") {
        fun createRoute(position: Int, title: String = "", author: String = "") =
            "loan/queue/$position?title=${Uri.encode(title)}&author=${Uri.encode(author)}"
    }

    // Telas de denúncia de comentário
    object ReportComment : Screen("report/{commentId}") {
        fun createRoute(commentId: String) = "report/$commentId"
    }
    object ReportConfirmation : Screen("report/confirmation")

    // Telas de Meus Empréstimos
    object MyLoans : Screen("myloans")
    object FinesPolicy : Screen("fines")
    object RenewalAvailable : Screen("renewal/available")
    object RenewalUnavailable : Screen("renewal/unavailable")

    // Telas Admin
    object AdminLoansPanel : Screen("admin/emprestimos")
}
