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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unifor.booksapp.BuildConfig
import com.unifor.booksapp.R
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.HomeUiState
import com.unifor.booksapp.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToBookDetails: (String) -> Unit = {},
    onNavigateToLoanApproved: () -> Unit = {},
    onNavigateToLoanUnavailable: () -> Unit = {},
    onNavigateToLoanQueue: () -> Unit = {},
    onNavigateToReportComment: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onNavigateToCatalog = onNavigateToCatalog,
                onNavigateToBookDetails = { onNavigateToBookDetails("dev") }, // Mantém o atalho do dev menu
                onNavigateToLoanApproved = onNavigateToLoanApproved,
                onNavigateToLoanUnavailable = onNavigateToLoanUnavailable,
                onNavigateToLoanQueue = onNavigateToLoanQueue,
                onNavigateToReportComment = onNavigateToReportComment,
                onNavigateToLogin = onNavigateToLogin
            )
        },
        containerColor = UniforBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HomeUiState.Error -> {
                    Text(
                        text = "Erro: ${state.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is HomeUiState.Success -> {
                    HomeScreenContent(
                        books = state.books,
                        onBookClick = onNavigateToBookDetails
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    books: List<Book>,
    onBookClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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

        TopRatedCarousel(books = books, onBookClick = onBookClick)

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(48.dp))
            DiscoverMoreSection()
            Spacer(modifier = Modifier.height(48.dp))
            NewReleasesCarousel(books = books, onBookClick = onBookClick)
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onNavigateToCatalog: () -> Unit,
    onNavigateToBookDetails: () -> Unit,
    onNavigateToLoanApproved: () -> Unit,
    onNavigateToLoanUnavailable: () -> Unit,
    onNavigateToLoanQueue: () -> Unit,
    onNavigateToReportComment: () -> Unit,
    onNavigateToLogin: () -> Unit
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

            if (BuildConfig.DEBUG) {
                DevShortcutsMenu(
                    onNavigateToCatalog,
                    onNavigateToBookDetails,
                    onNavigateToLoanApproved,
                    onNavigateToLoanUnavailable,
                    onNavigateToLoanQueue,
                    onNavigateToReportComment,
                    onNavigateToLogin
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

@Composable
private fun DevShortcutsMenu(
    onNavigateToCatalog: () -> Unit,
    onNavigateToBookDetails: () -> Unit,
    onNavigateToLoanApproved: () -> Unit,
    onNavigateToLoanUnavailable: () -> Unit,
    onNavigateToLoanQueue: () -> Unit,
    onNavigateToReportComment: () -> Unit,
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
            DropdownMenuItem(text = { Text("Catálogo") }, onClick = { expanded = false; onNavigateToCatalog() })
            DropdownMenuItem(text = { Text("Detalhes do Livro") }, onClick = { expanded = false; onNavigateToBookDetails() })
            HorizontalDivider()
            DropdownMenuItem(text = { Text("Empréstimo Aprovado") }, onClick = { expanded = false; onNavigateToLoanApproved() })
            DropdownMenuItem(text = { Text("Livro Indisponível") }, onClick = { expanded = false; onNavigateToLoanUnavailable() })
            DropdownMenuItem(text = { Text("Você está na Fila") }, onClick = { expanded = false; onNavigateToLoanQueue() })
            HorizontalDivider()
            DropdownMenuItem(text = { Text("Denunciar Comentário") }, onClick = { expanded = false; onNavigateToReportComment() })
            HorizontalDivider()
            DropdownMenuItem(text = { Text("Voltar ao Login") }, onClick = { expanded = false; onNavigateToLogin() })
        }
    }
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
fun TopRatedCarousel(
    books: List<Book>,
    onBookClick: (String) -> Unit
) {
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
            items(books) { book ->
                BookCard(
                    book = book,
                    onClick = { onBookClick(book.id) }
                )
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.capaUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Capa do livro ${book.titulo}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )

            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd),
                color = if (book.exemplaresDisponiveis > 0) UniforSecondaryContainer else Color.LightGray,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    if (book.exemplaresDisponiveis > 0) "DISPONÍVEL" else "INDISPONÍVEL",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp, fontWeight = FontWeight.Black, color = UniforOnSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(book.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
        Text(book.autor, color = UniforOutline, fontSize = 13.sp, maxLines = 1)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            val rating = book.mediaAvaliacao.toInt() / 2
            repeat(rating) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = UniforTertiaryFixed,
                    modifier = Modifier.size(16.dp)
                )
            }
            repeat(5 - rating) {
                Icon(
                    Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = UniforTertiaryFixed,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

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
fun NewReleasesCarousel(
    books: List<Book>,
    onBookClick: (String) -> Unit
) {
    Column {
        Text("Novidades", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary)
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(books) { book ->
                Column(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { onBookClick(book.id) }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(book.capaUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "Capa do livro ${book.titulo}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.75f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "RECÉM CHEGADO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = UniforSecondary
                    )
                    Text(book.titulo, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                }
            }
        }
    }
}
