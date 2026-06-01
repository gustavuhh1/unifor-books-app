package com.unifor.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unifor.booksapp.navigation.Screen
import com.unifor.booksapp.ui.components.UniforBottomNavBar
import com.unifor.booksapp.ui.screens.*
import com.unifor.booksapp.ui.theme.UniforBooksAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniforBooksAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Lê o role atualizado a cada mudança de rota (inclui pós-login)
                val isAdmin by remember(currentRoute) {
                    derivedStateOf {
                        (application as UniforBooksApp).sessionManager.getUserRole() == "ADMIN"
                    }
                }

                val hideBottomBarRoutes = setOf(
                    Screen.Login.route,
                    Screen.LoanApproved.route,
                    Screen.LoanUnavailable.route,
                    Screen.LoanQueue.route,
                    Screen.ReportComment.route,
                    Screen.ReportConfirmation.route,
                    Screen.FinesPolicy.route,
                    Screen.RenewalAvailable.route,
                    Screen.RenewalUnavailable.route,
                    Screen.Profile.route,
                    Screen.ChangePassword.route,
                    Screen.ChangePasswordSuccess.route
                )
                val showBottomBar = currentRoute != null &&
                        hideBottomBarRoutes.none { currentRoute.startsWith(it.substringBefore("{")) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            UniforBottomNavBar(
                                currentRoute = currentRoute,
                                isAdmin = isAdmin,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        // Auth
                        composable(Screen.Login.route) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }

                        // Main Screens
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToBookDetails = { bookId ->
                                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                                }
                            )
                        }
                        composable(Screen.Catalog.route) {
                            CatalogScreen(
                                onBookClick = { bookId ->
                                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                                },
                                onBack = { navController.popBackStack() },
                                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                            )
                        }
                        composable(
                            route = Screen.BookDetails.route,
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            BookDetailScreen(
                                bookId = bookId,
                                onBack = { navController.popBackStack() },
                                onReportComment = { commentId ->
                                    navController.navigate(Screen.ReportComment.createRoute(commentId))
                                },
                                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                                onNavigateToLoanApproved = { title, author, deadline ->
                                    navController.navigate(Screen.LoanApproved.createRoute(title, author, deadline))
                                },
                                onNavigateToLoanUnavailable = { title, author ->
                                    navController.navigate(Screen.LoanUnavailable.createRoute(title, author))
                                },
                                onNavigateToLoanQueue = { position, title, author ->
                                    navController.navigate(Screen.LoanQueue.createRoute(position, title, author))
                                }
                            )
                        }

                        // Profile
                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                onBack = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onChangePassword = { navController.navigate(Screen.ChangePassword.route) }
                            )
                        }

                        // Change Password Flow
                        composable(Screen.ChangePassword.route) {
                            ChangePasswordScreen(
                                onBack = { navController.popBackStack() },
                                onPasswordChanged = {
                                    navController.navigate(Screen.ChangePasswordSuccess.route) {
                                        popUpTo(Screen.ChangePassword.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Screen.ChangePasswordSuccess.route) {
                            ChangePasswordSuccessScreen(
                                onBackToProfile = {
                                    navController.navigate(Screen.Profile.route) {
                                        popUpTo(Screen.ChangePasswordSuccess.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // My Loans Flow
                        composable(Screen.MyLoans.route) {
                            MyLoansScreen(
                                onNavigateToFines = { navController.navigate(Screen.FinesPolicy.route) },
                                onNavigateToRenewal = { isAvailable ->
                                    if (isAvailable) navController.navigate(Screen.RenewalAvailable.route)
                                    else navController.navigate(Screen.RenewalUnavailable.route)
                                },
                                onBack = { navController.popBackStack() },
                                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                            )
                        }
                        composable(Screen.FinesPolicy.route) {
                            FinesPolicyScreen(onBack = { navController.popBackStack() })
                        }
                        composable(Screen.RenewalAvailable.route) {
                            RenewalAvailableScreen(
                                onBack = { navController.popBackStack() },
                                onGoToCollection = {},
                                newDueDate = null
                            )
                        }
                        composable(Screen.RenewalUnavailable.route) {
                            RenewalUnavailableScreen(
                                onBack = { navController.popBackStack() },
                                onGoToCollection = {}
                            )
                        }

                        // Loan Status
                        composable(
                            route = Screen.LoanApproved.route,
                            arguments = listOf(
                                navArgument("title") { type = NavType.StringType; nullable = true; defaultValue = "" },
                                navArgument("author") { type = NavType.StringType; nullable = true; defaultValue = "" },
                                navArgument("deadline") { type = NavType.StringType; nullable = true; defaultValue = "" }
                            )
                        ) { backStackEntry ->
                            LoanApprovedScreen(
                                bookTitle = backStackEntry.arguments?.getString("title") ?: "",
                                bookAuthor = backStackEntry.arguments?.getString("author") ?: "",
                                pickupDeadline = backStackEntry.arguments?.getString("deadline")?.takeIf { it.isNotEmpty() },
                                onViewLoans = { navController.navigate(Screen.MyLoans.route) },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.LoanUnavailable.route,
                            arguments = listOf(
                                navArgument("title") { type = NavType.StringType; nullable = true; defaultValue = "" },
                                navArgument("author") { type = NavType.StringType; nullable = true; defaultValue = "" }
                            )
                        ) { backStackEntry ->
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val author = backStackEntry.arguments?.getString("author") ?: ""
                            LoanUnavailableScreen(
                                bookTitle = title,
                                bookAuthor = author,
                                onBack = { navController.popBackStack() },
                                onJoinQueue = {
                                    navController.navigate(Screen.LoanQueue.createRoute(1, title, author))
                                }
                            )
                        }
                        composable(
                            route = Screen.LoanQueue.route,
                            arguments = listOf(
                                navArgument("position") { type = NavType.IntType },
                                navArgument("title") { type = NavType.StringType; nullable = true; defaultValue = "" },
                                navArgument("author") { type = NavType.StringType; nullable = true; defaultValue = "" }
                            )
                        ) { backStackEntry ->
                            LoanQueueScreen(
                                queuePosition = backStackEntry.arguments?.getInt("position"),
                                bookTitle = backStackEntry.arguments?.getString("title") ?: "",
                                bookAuthor = backStackEntry.arguments?.getString("author") ?: "",
                                onBack = { navController.popBackStack() },
                                onViewLoans = { navController.navigate(Screen.MyLoans.route) }
                            )
                        }

                        // Report Flow
                        composable(Screen.ReportComment.route) {
                            ReportCommentScreen(
                                onSendReport = {
                                    navController.navigate(Screen.ReportConfirmation.route) {
                                        popUpTo(Screen.ReportComment.route) { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.ReportConfirmation.route) {
                            ReportConfirmationScreen(
                                onBackToBook = {
                                    navController.popBackStack(
                                        Screen.BookDetails.route.substringBefore("{"),
                                        false
                                    )
                                },
                                onGoToHome = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // Admin
                        composable(Screen.AdminLoansPanel.route) {
                            AdminLoansPanelScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
