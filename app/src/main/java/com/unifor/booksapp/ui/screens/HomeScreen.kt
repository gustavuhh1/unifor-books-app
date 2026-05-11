package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.unifor.booksapp.ui.theme.*

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { HomeTopBar() },
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
                NewReleasesCarousel()
                Spacer(modifier = Modifier.height(120.dp)) // Espaço para BottomNav
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
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
        val categories = listOf(
            "Computação" to Icons.Default.Computer,
            "Arquitetura" to Icons.Default.Architecture,
            "Direito" to Icons.Default.Gavel,
            "Medicina" to Icons.Default.MedicalServices,
            "Economia" to Icons.Default.AccountBalance,
            "Psicologia" to Icons.Default.Psychology
        )
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
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            items(5) { BookCard() }
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
                    "DISPONÍVEL",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp, fontWeight = FontWeight.Black, color = UniforOnSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Inteligência Artificial", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text("Dr. Alan Turing", color = UniforOutline, fontSize = 13.sp)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            repeat(5) { Icon(Icons.Default.Star, contentDescription = null, tint = UniforTertiaryFixed, modifier = Modifier.size(16.dp)) }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Mural de Eventos
// ──────────────────────────────────────────────────────────────────────────────

/**
 * Tipo de evento exibido no Mural.
 *
 * Cada tipo carrega sua própria paleta visual conforme o protótipo:
 *  - WORKSHOP   → fundo azul escuro, texto branco
 *  - LANCAMENTO → fundo amarelo claro, texto azul escuro
 */
enum class EventType(
    val containerColor: Color,
    val onContainerColor: Color,
    val badgeColor: Color,
    val onBadgeColor: Color,
    val secondaryTextColor: Color,
    val badgeText: String
) {
    WORKSHOP(
        containerColor = UniforPrimary,
        onContainerColor = Color.White,
        badgeColor = UniforSecondary,
        onBadgeColor = Color.White,
        secondaryTextColor = Color.White.copy(alpha = 0.75f),
        badgeText = "PRÓXIMO WORKSHOP"
    ),
    LANCAMENTO(
        containerColor = Color(0xFFFFE7A1),       // amarelo claro (tertiary container)
        onContainerColor = UniforPrimary,
        badgeColor = UniforTertiaryFixed,         // amarelo Unifor
        onBadgeColor = Color(0xFF3F2E00),
        secondaryTextColor = UniforPrimary.copy(alpha = 0.7f),
        badgeText = "LANÇAMENTO"
    )
}

/**
 * Modelo de evento exibido no Mural.
 *
 * Será preenchido pelo banco de dados. Por ora, a lista virá vazia.
 */
data class MuralEvent(
    val id: String,
    val type: EventType,
    val title: String,
    val description: String,
    val dateInfo: String
)

@Composable
fun EventsMural(
    events: List<MuralEvent> = emptyList()
) {
    Column {
        Text("Mural de Eventos", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            events.forEach { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
private fun EventCard(event: MuralEvent) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = event.type.containerColor,
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            // Badge
            Surface(color = event.type.badgeColor, shape = CircleShape) {
                Text(
                    event.type.badgeText,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = event.type.onBadgeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                event.title,
                color = event.type.onContainerColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Descrição
            Text(
                event.description,
                color = event.type.secondaryTextColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Linha de data
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = event.type.onContainerColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    event.dateInfo,
                    color = event.type.onContainerColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun DiscoverMoreSection() {
    Column {
        Text("Descubra Mais", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        repeat(2) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = Color(0xFFF3F4F5),
                shape = RoundedCornerShape(20.dp),
                onClick = { }
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp, 80.dp).background(UniforOutline.copy(alpha = 0.2f), RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Marketing Estratégico", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Recomendado com base no seu histórico", fontSize = 12.sp, color = UniforOutline)
                    }
                }
            }
        }
    }
}

@Composable
fun NewReleasesCarousel() {
    Column {
        Text("Novidades", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(5) {
                Column(modifier = Modifier.width(140.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.75f).background(UniforSurfaceContainerHigh, RoundedCornerShape(12.dp)))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("RECÉM CHEGADO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UniforSecondary)
                    Text("Data Science Pro", fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                }
            }
        }
    }
}
