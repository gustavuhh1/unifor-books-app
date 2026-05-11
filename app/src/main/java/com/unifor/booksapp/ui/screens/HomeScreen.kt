package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.BuildConfig
import com.unifor.booksapp.ui.theme.*

/**
 * Tela principal.
 *
 * Recebe callbacks de navegação que são consumidos pelos atalhos de
 * desenvolvedor (visíveis apenas em builds de DEBUG).
 */
@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToBookDetails: () -> Unit = {},
    onNavigateToLoanApproved: () -> Unit = {},
    onNavigateToLoanUnavailable: () -> Unit = {},
    onNavigateToLoanQueue: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                onNavigateToCatalog = onNavigateToCatalog,
                onNavigateToBookDetails = onNavigateToBookDetails,
                onNavigateToLoanApproved = onNavigateToLoanApproved,
                onNavigateToLoanUnavailable = onNavigateToLoanUnavailable,
                onNavigateToLoanQueue = onNavigateToLoanQueue,
                onNavigateToLogin = onNavigateToLogin
            )
        },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                WelcomeHeader()
                Spacer(modifier = Modifier.height(32.dp))
                SearchSection()
                Spacer(modifier = Modifier.height(48.dp))
                CategoriesBentoGrid()
                Spacer(modifier = Modifier.height(48.dp))
            }

            TopRatedCarousel()

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(48.dp))
                EventsMural()
                Spacer(modifier = Modifier.height(48.dp))
                DiscoverMoreSection()
                Spacer(modifier = Modifier.height(48.dp))
            }

            NewReleasesCarousel()

            Spacer(modifier = Modifier.height(120.dp)) // Espaço para BottomNav
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToBookDetails: () -> Unit = {},
    onNavigateToLoanApproved: () -> Unit = {},
    onNavigateToLoanUnavailable: () -> Unit = {},
    onNavigateToLoanQueue: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = UniforPrimary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Unifor Books", fontWeight = FontWeight.Black, fontSize = 20.sp, color = UniforPrimary)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = UniforOutline)
            }

            // Atalhos de desenvolvimento (apenas em DEBUG)
            if (BuildConfig.DEBUG) {
                DevShortcutsMenu(
                    onNavigateToCatalog = onNavigateToCatalog,
                    onNavigateToBookDetails = onNavigateToBookDetails,
                    onNavigateToLoanApproved = onNavigateToLoanApproved,
                    onNavigateToLoanUnavailable = onNavigateToLoanUnavailable,
                    onNavigateToLoanQueue = onNavigateToLoanQueue,
                    onNavigateToLogin = onNavigateToLogin
                )
            }

            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(UniforSurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground.copy(alpha = 0.8f))
    )
}

/**
 * Menu de atalhos para desenvolvedores.
 *
 * Permite navegar diretamente para qualquer tela do app, útil enquanto o
 * banco de dados não está alimentado. Visível apenas em builds DEBUG.
 */
@Composable
private fun DevShortcutsMenu(
    onNavigateToCatalog: () -> Unit,
    onNavigateToBookDetails: () -> Unit,
    onNavigateToLoanApproved: () -> Unit,
    onNavigateToLoanUnavailable: () -> Unit,
    onNavigateToLoanQueue: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.DeveloperMode,
                contentDescription = "Atalhos de desenvolvimento",
                tint = UniforPrimary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Cabeçalho
            Text(
                "ATALHOS DEV",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = UniforOutline,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()

            DevMenuItem(
                label = "Catálogo",
                icon = Icons.Default.ImportContacts,
                onClick = {
                    expanded = false
                    onNavigateToCatalog()
                }
            )
            DevMenuItem(
                label = "Detalhes do Livro",
                icon = Icons.Default.MenuBook,
                onClick = {
                    expanded = false
                    onNavigateToBookDetails()
                }
            )

            HorizontalDivider()

            DevMenuItem(
                label = "Empréstimo Aprovado",
                icon = Icons.Default.CheckCircle,
                onClick = {
                    expanded = false
                    onNavigateToLoanApproved()
                }
            )
            DevMenuItem(
                label = "Livro Indisponível",
                icon = Icons.Default.Block,
                onClick = {
                    expanded = false
                    onNavigateToLoanUnavailable()
                }
            )
            DevMenuItem(
                label = "Você está na Fila",
                icon = Icons.Default.People,
                onClick = {
                    expanded = false
                    onNavigateToLoanQueue()
                }
            )

            HorizontalDivider()

            DevMenuItem(
                label = "Voltar ao Login",
                icon = Icons.Default.Logout,
                onClick = {
                    expanded = false
                    onNavigateToLogin()
                }
            )
        }
    }
}

@Composable
private fun DevMenuItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = UniforPrimary)
        },
        onClick = onClick
    )
}

@Composable
fun WelcomeHeader() {
    Column {
        Text("Página Inicial", fontSize = 36.sp, fontWeight = FontWeight.Black, color = UniforPrimary, letterSpacing = (-1).sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Procure e avalie nossos livros e faça seu empréstimo. Sua jornada acadêmica começa aqui.",
            fontSize = 18.sp, color = UniforOutline, lineHeight = 26.sp
        )
    }
}

@Composable
fun SearchSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(UniforSurfaceContainerHigh, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = UniforOutline)
        Spacer(modifier = Modifier.width(12.dp))
        Text("Pesquisar títulos, autores ou ISBN...", modifier = Modifier.weight(1f), color = UniforOutline.copy(alpha = 0.6f))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text("Buscar", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoriesBentoGrid() {
    Column {
        Text("Categorias de Estudo", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        // Categorias virão do banco. Placeholder de grade vazia.
        val categories = emptyList<Pair<String, ImageVector>>()
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (i in categories.indices step 2) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CategoryItem(categories[i].first, categories[i].second, Modifier.weight(1f))
                    if (i + 1 < categories.size) {
                        CategoryItem(categories[i + 1].first, categories[i + 1].second, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(110.dp),
        color = Color(0xFFF3F4F5),
        shape = RoundedCornerShape(20.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = UniforPrimary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun TopRatedCarousel() {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Livros Mais Bem Avaliados", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
            Text("Ver todos", color = UniforPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Lista de livros mais bem avaliados virá do banco.
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // items virão do banco
        }
    }
}

@Composable
fun BookCard() {
    Column(modifier = Modifier.width(200.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .background(UniforSurfaceContainerHigh, RoundedCornerShape(20.dp))
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd),
                color = UniforSecondaryContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp, fontWeight = FontWeight.Black, color = UniforOnSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text("", color = UniforOutline, fontSize = 13.sp)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            repeat(5) {
                Icon(Icons.Default.Star, contentDescription = null, tint = UniforTertiaryFixed, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun EventsMural() {
    // Eventos da biblioteca virão do banco. Lista vazia por enquanto.
    val events = emptyList<Unit>()

    Column {
        Text("Mural de Eventos", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            events.forEach { _ ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = UniforPrimary,
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Surface(color = UniforSecondary, shape = CircleShape) {
                            Text(
                                "",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverMoreSection() {
    // Sugestões virão do banco. Lista vazia por enquanto.
    val items = emptyList<Unit>()

    Column {
        Text("Descubra Mais", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        items.forEach { _ ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = Color(0xFFF3F4F5),
                shape = RoundedCornerShape(20.dp),
                onClick = { }
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp, 80.dp)
                            .background(UniforOutline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("", fontSize = 12.sp, color = UniforOutline)
                    }
                }
            }
        }
    }
}

@Composable
fun NewReleasesCarousel() {
    Column {
        Text(
            "Novidades",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = UniforPrimary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Lançamentos virão do banco.
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // items virão do banco
        }
    }
}
