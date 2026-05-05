package com.unifor.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unifor.booksapp.navigation.Screen
import com.unifor.booksapp.ui.components.UniforBottomNavBar
import com.unifor.booksapp.ui.screens.HomeScreen
import com.unifor.booksapp.ui.screens.LoginScreen
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

                // Só mostramos a NavBar se não estivermos na tela de Login
                val showBottomBar = currentRoute != Screen.Login.route

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
                        composable(Screen.Login.route) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            })
                        }
                        composable(Screen.Home.route) {
                            HomeScreen()
                        }
                        // Outras rotas serão adicionadas aqui (Catalog, Loans, Profile)
                    }
                }
            }
        }
    }
}
