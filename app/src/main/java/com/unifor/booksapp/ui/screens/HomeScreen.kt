package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unifor.booksapp.R
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.HomeUiState
import com.unifor.booksapp.ui.viewmodels.HomeViewModel

// Cores Extraídas do Código React (Design Pixel-Perfect)
private val ReactBg = Color(0xFFF8F9FA)
private val ReactPrimary = Color(0xFF00346F)
private val ReactSubtitle = Color(0xFF424751)
private val ReactSearchBg = Color(0xFFE7E8E9)
private val ReactSearchPlaceholder = Color(0xFFC2C6D3)
private val ReactCategoryBg = Color(0xFFF3F4F5)
private val ReactBookCardBg = Color(0xFFEDEEEF)
private val ReactAvailableBadgeBg = Color(0xFF8AFAA7)
private val ReactAvailableBadgeText = Color(0xFF007439)
private val ReactUnavailableBadgeBg = Color(0xFFE6E7E8)
private val ReactUnavailableBadgeText = Color(0xFF444751)
private val ReactNewTag = Color(0xFF006D35)
private val ReactStar = Color(0xFFF3B01F)

@Composable
fun HomeScreen(
    onNavigateToBookDetails: (String) -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToLoans: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    // Surface raiz com cor explícita para evitar "tela preta" se o tema falhar ou estiver em Dark Mode
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ReactBg
    ) {
        Scaffold(
            topBar = { HomeTopBar(onNavigateToProfile = onNavigateToProfile) },
            containerColor = Color.Transparent // Surface já define a cor
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val state = uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = ReactPrimary
                        )
                    }
                    is HomeUiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Erro ao carregar livros", color = MaterialTheme.colorScheme.error)
                            TextButton(onClick = homeViewModel::fetchBooks) {
                                Text("Tentar novamente", color = ReactPrimary)
                            }
                        }
                    }
                    is HomeUiState.Success -> {
                        HomeContent(
                            books = state.books,
                            onBookClick = onNavigateToBookDetails,
                            onNavigateToCatalog = onNavigateToCatalog
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    books: List<Book>,
    onBookClick: (String) -> Unit,
    onNavigateToCatalog: () -> Unit
) {
    val topRated = remember(books) { books.sortedByDescending { it.mediaAvaliacao } }
    val newReleases = remember(books) { books.sortedByDescending { it.criadoEm } }
    val discover = remember(books) { books.shuffled().take(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp) // Espaçamento extra para não cobrir pela bottom bar
    ) {
        // ── Cabeçalho (pt-8 px-6) ─────────────────────────────
        Column(modifier = Modifier.padding(top = 32.dp, start = 24.dp, end = 24.dp)) {
            Text(
                text = "Página Inicial",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = ReactPrimary,
                letterSpacing = (-0.9).sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Procure e avalie nossos livros e faça\nseu empréstimo. Sua jornada\nacadêmica começa aqui.",
                fontSize = 18.sp,
                color = ReactSubtitle,
                lineHeight = 29.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Barra de Pesquisa (h-[63px] px-6) ─────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            HomeSearchBar()
        }

        Spacer(modifier = Modifier.height(48.dp)) // gap-12

        // ── Categorias de Estudo (gap-6) ──────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Categorias de Estudo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ReactPrimary
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesGrid()
        }

        Spacer(modifier = Modifier.height(48.dp))

        // ── Livros Mais Bem Avaliados ─────────────────────────
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Livros Mais Bem Avaliados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ReactPrimary
                )
                Text(
                    text = "Ver todos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ReactPrimary,
                    modifier = Modifier.clickable { onNavigateToCatalog() }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(topRated) { book ->
                    TopRatedBookCard(book = book, onClick = { onBookClick(book.id) })
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // ── Descubra Mais ─────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Descubra Mais",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ReactPrimary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                discover.forEachIndexed { index, book ->
                    DiscoverBookItem(
                        book = book,
                        subtitle = if (index == 0) "Recomendado com base no seu histórico" 
                                   else "Livro popular em ${book.categoria}",
                        onClick = { onBookClick(book.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // ── Novidades ─────────────────────────────────────────
        Column {
            Text(
                text = "Novidades",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ReactPrimary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(newReleases) { book ->
                    NewReleaseBookCard(book = book, onClick = { onBookClick(book.id) })
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// COMPONENTES DE UI (Fidelidade ao React)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onNavigateToProfile: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(72.dp),
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = 8.dp // Reflete shadow do React
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painter = painterResource(R.drawable.ic_unifor_logo), contentDescription = null, modifier = Modifier.size(40.dp))
                Text("Unifor Books", fontSize = 20.sp, fontWeight = FontWeight.Black, color = ReactPrimary, letterSpacing = (-0.5).sp)
            }
            Box(
                modifier = Modifier.size(45.dp).clip(RoundedCornerShape(50)).background(ReactSearchBg).clickable { onNavigateToProfile() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "Perfil", tint = ReactPrimary, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun HomeSearchBar() {
    Box(modifier = Modifier.fillMaxWidth().height(63.dp)) {
        Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(ReactSearchBg))
        Row(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = Color(0xFF737783), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Pesquisar títulos, autores ou ISBN...", fontSize = 16.sp, color = ReactSearchPlaceholder, modifier = Modifier.weight(1f))
            Button(
                onClick = { },
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ReactPrimary),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text("Buscar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
private fun CategoriesGrid() {
    val cats = listOf(
        "Computação" to Icons.Default.Computer, "Arquitetura" to Icons.Default.Architecture,
        "Direito" to Icons.Default.Gavel, "Medicina" to Icons.Default.MedicalServices,
        "Economia" to Icons.Default.AccountBalance, "Psicologia" to Icons.Default.Psychology
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (i in cats.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CategoryItem(cats[i].first, cats[i].second, Modifier.weight(1f))
                if (i + 1 < cats.size) CategoryItem(cats[i+1].first, cats[i+1].second, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CategoryItem(label: String, icon: ImageVector, modifier: Modifier) {
    Surface(modifier = modifier.height(108.dp), color = ReactCategoryBg, shape = RoundedCornerShape(8.dp), onClick = {}) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = ReactPrimary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1D))
        }
    }
}

@Composable
private fun TopRatedBookCard(book: Book, onClick: () -> Unit) {
    Column(modifier = Modifier.width(200.dp).clickable { onClick() }, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(267.dp).clip(RoundedCornerShape(8.dp)).background(ReactBookCardBg)) {
            BookCoverImage(book.capaUrl, book.titulo, Modifier.fillMaxSize(), 8)
            val avail = book.exemplaresDisponiveis > 0
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                color = if (avail) ReactAvailableBadgeBg else ReactUnavailableBadgeBg,
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = if (avail) "DISPONÍVEL" else "INDISPONÍVEL",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    color = if (avail) ReactAvailableBadgeText else ReactUnavailableBadgeText,
                    letterSpacing = 0.5.sp
                )
            }
        }
        Text(book.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1D), maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(book.autor, fontSize = 12.sp, color = ReactSubtitle, maxLines = 1)
        Row { repeat(5) { i -> Icon(Icons.Default.Star, null, tint = ReactStar, modifier = Modifier.size(12.dp)) } }
    }
}

@Composable
private fun DiscoverBookItem(book: Book, subtitle: String, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable { onClick() }, color = ReactCategoryBg, shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BookCoverImage(book.capaUrl, book.titulo, Modifier.size(64.dp, 80.dp), 4)
            Column(modifier = Modifier.weight(1f)) {
                Text(book.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1D), maxLines = 1)
                Text(subtitle, fontSize = 12.sp, color = ReactSubtitle, maxLines = 2)
            }
        }
    }
}

@Composable
private fun NewReleaseBookCard(book: Book, onClick: () -> Unit) {
    Column(modifier = Modifier.width(140.dp).clickable { onClick() }, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(187.dp).clip(RoundedCornerShape(4.dp)).background(ReactBookCardBg)) {
            BookCoverImage(book.capaUrl, book.titulo, Modifier.fillMaxSize(), 4)
        }
        Text("RECÉM CHEGADO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ReactNewTag)
        Text(book.titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1D), maxLines = 1)
    }
}

@Composable
fun BookCoverImage(url: String?, title: String, modifier: Modifier, corner: Int) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(corner.dp)),
        error = painterResource(R.drawable.ic_launcher_background)
    )
}
