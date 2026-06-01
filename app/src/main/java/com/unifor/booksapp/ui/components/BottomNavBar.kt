package com.unifor.booksapp.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.navigation.Screen
import com.unifor.booksapp.ui.theme.UniforPrimary
import com.unifor.booksapp.ui.theme.UniforOutline

sealed class NavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavItem(Screen.Home.route, Icons.Default.Home, "Início")
    object Catalog : NavItem(Screen.Catalog.route, Icons.Default.ImportContacts, "Catálogo")
    object Loans : NavItem(Screen.MyLoans.route, Icons.Default.MenuBook, "Empréstimos")
    object Profile : NavItem(Screen.Profile.route, Icons.Default.Person, "Perfil")
    // Itens exclusivos para admin
    object AdminLoans : NavItem(Screen.AdminLoansPanel.route, Icons.Default.MenuBook, "Empréstimos")
    object Admin : NavItem(Screen.Profile.route, Icons.Default.AdminPanelSettings, "Admin")
}

@Composable
fun UniforBottomNavBar(
    currentRoute: String?,
    isAdmin: Boolean = false,
    onNavigate: (String) -> Unit
) {
    // Admin vê: Início | Catálogo | Empréstimos → painel admin | Admin → perfil
    val items = if (isAdmin) {
        listOf(NavItem.Home, NavItem.Catalog, NavItem.AdminLoans, NavItem.Admin)
    } else {
        listOf(NavItem.Home, NavItem.Catalog, NavItem.Loans, NavItem.Profile)
    }

    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) UniforPrimary else UniforOutline
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) UniforPrimary else UniforOutline
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = UniforPrimary.copy(alpha = 0.08f)
                )
            )
        }
    }
}

