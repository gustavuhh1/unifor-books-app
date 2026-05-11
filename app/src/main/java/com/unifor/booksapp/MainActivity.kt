package com.unifor.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
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

                // Rotas que não devem exibir a BottomNav
                val hideBottomBarRoutes = setOf(
                    Screen.Login.route,
                    Screen.LoanApproved.route,
                    Screen.LoanUnavailable.route,
                    Screen.LoanQueue.route   // rota-template sem parâmetro
                )
                val showBottomBar = currentRoute != null &&
                        hideBottomBarRoutes.none { currentRoute.startsWith(it.substringBefore("{")) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            UniforBottomNavBar(
                                currentRoute = currentRoute,
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
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // ── Auth ──────────────────────────────────────────
                        composable(Screen.Login.route) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }

                        // ── Main ─────────────────────────────────────────
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToCatalog = {
                                    navController.navigate(Screen.Catalog.route)
                                },
                                onNavigateToBookDetails = {
                                    // Usa um id-dummy enquanto o BD não fornece livros reais
                                    navController.navigate(Screen.BookDetails.createRoute("dev"))
                                },
                                onNavigateToLoanApproved = {
                                    navController.navigate(Screen.LoanApproved.route)
                                },
                                onNavigateToLoanUnavailable = {
                                    navController.navigate(Screen.LoanUnavailable.route)
                                },
                                onNavigateToLoanQueue = {
                                    navController.navigate(Screen.LoanQueue.createRoute(3))
                                },
                                onNavigateToLogin = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Catalog.route) {
                            CatalogScreen(
                                onBookClick = { bookId ->
                                    navController.navigate(Screen.BookDetails.createRoute(bookId))
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.BookDetails.route) {
                            BookDetailScreen(onBack = { navController.popBackStack() })
                        }

                        // ── Loan status ───────────────────────────────────
                        composable(Screen.LoanApproved.route) {
                            LoanApprovedScreen(
                                onViewLoans = {
                                    navController.navigate("loans") {
                                        launchSingleTop = true
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.LoanUnavailable.route) {
                            LoanUnavailableScreen(
                                onBack = { navController.popBackStack() },
                                onViewDigitalCollection = {
                                    navController.navigate(Screen.Catalog.route) {
                                        launchSingleTop = true
                                    }
                                },
                                onJoinQueue = {
                                    navController.navigate(Screen.LoanQueue.createRoute(3)) {
                                        popUpTo(Screen.LoanUnavailable.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.LoanQueue.route,
                            arguments = listOf(navArgument("position") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val position = backStackEntry.arguments?.getInt("position") ?: 1
                            LoanQueueScreen(
                                queuePosition = position,
                                onViewLoans = {
                                    navController.navigate("loans") {
                                        launchSingleTop = true
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // Outras rotas serão adicionadas aqui (Loans, Profile)
                    }
                }
            }
        }
    }
}
