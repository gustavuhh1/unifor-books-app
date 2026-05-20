package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unifor.booksapp.R
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.BookDetailUiState
import com.unifor.booksapp.ui.viewmodels.BookDetailViewModel
import com.unifor.booksapp.ui.viewmodels.EmprestimoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: String,
    onBack: () -> Unit,
    onReportComment: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit,
    onNavigateToLoanApproved: () -> Unit = {},
    onNavigateToLoanUnavailable: () -> Unit = {},
    onNavigateToLoanQueue: (Int) -> Unit = {},
    bookDetailViewModel: BookDetailViewModel = viewModel()
) {
    val bookState by bookDetailViewModel.bookState.collectAsState()
    val emprestimoState by bookDetailViewModel.emprestimoState.collectAsState()

    LaunchedEffect(bookId) {
        bookDetailViewModel.loadBook(bookId)
    }

    LaunchedEffect(emprestimoState) {
        when (val state = emprestimoState) {
            is EmprestimoUiState.NaFila -> {
                bookDetailViewModel.resetEmprestimoState()
                onNavigateToLoanQueue(state.posicao)
            }
            is EmprestimoUiState.Indisponivel -> {
                bookDetailViewModel.resetEmprestimoState()
                onNavigateToLoanUnavailable()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { BookDetailTopBar(onBack = onBack, onNavigateToProfile = onNavigateToProfile) },
        containerColor = UniforBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = bookState) {
                is BookDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BookDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Erro ao carregar livro",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { bookDetailViewModel.loadBook(bookId) }) {
                            Text("Tentar novamente", color = UniforPrimary)
                        }
                    }
                }
                is BookDetailUiState.Success -> {
                    BookDetailContent(
                        book = state.book,
                        emprestimoLoading = emprestimoState is EmprestimoUiState.Loading,
                        onSolicitarEmprestimo = { bookDetailViewModel.solicitarEmprestimo(state.book) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookDetailContent(
    book: Book,
    emprestimoLoading: Boolean,
    onSolicitarEmprestimo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BookCoverSection(book)
        Spacer(modifier = Modifier.height(16.dp))
        BookInfoSection(book)
        Spacer(modifier = Modifier.height(24.dp))
        BookDescriptionSection(
            book = book,
            emprestimoLoading = emprestimoLoading,
            onSolicitarEmprestimo = onSolicitarEmprestimo
        )
        if (book.mediaAvaliacao > 0.0) {
            Spacer(modifier = Modifier.height(24.dp))
            RatingSection(book)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
            }
        },
        actions = {
            Box(
                Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(UniforSurfaceContainerHigh)
                    .clickable { onNavigateToProfile() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

@Composable
private fun BookCoverSection(book: Book) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (book.capaUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.capaUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Capa de ${book.titulo}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(130.dp)
                    .height(190.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Surface(
                modifier = Modifier
                    .width(130.dp)
                    .height(190.dp),
                shape = RoundedCornerShape(8.dp),
                color = UniforSurface,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UniforPrimary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        book.titulo,
                        color = UniforPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookInfoSection(book: Book) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        val available = book.exemplaresDisponiveis > 0
        Surface(
            color = if (available) UniforSuccessContainer else Color(0xFFFFDAD6),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                if (available) "DISPONÍVEL" else "INDISPONÍVEL",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = if (available) UniforOnSecondaryContainer else Color(0xFFBA1A1A),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            book.titulo,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = UniforPrimary,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(book.autor, fontSize = 16.sp, color = UniforOutline)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (book.mediaAvaliacao > 0.0) {
                InfoItem(
                    value = "%.1f".format(book.mediaAvaliacao),
                    label = "AVALIAÇÃO",
                    caption = "${book.exemplaresDisponiveis}/${book.totalExemplares} disponíveis",
                    hasStars = true
                )
            } else {
                InfoItem(
                    value = "${book.exemplaresDisponiveis}/${book.totalExemplares}",
                    label = "DISPONÍVEIS",
                    caption = "exemplares"
                )
            }
            if (book.paginas != null) {
                InfoItem(
                    value = book.paginas.toString(),
                    label = "PÁGINAS",
                    caption = book.editora ?: ""
                )
            }
            if (book.anoPublicacao != null) {
                InfoItem(
                    value = book.anoPublicacao.toString(),
                    label = "ANO",
                    caption = book.idioma ?: ""
                )
            }
        }
    }
}

@Composable
private fun InfoItem(value: String, label: String, caption: String, hasStars: Boolean = false) {
    Column {
        if (hasStars) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = UniforTertiary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
            }
        } else {
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        }
        Text(label, fontSize = 11.sp, color = UniforOutline, letterSpacing = 0.5.sp)
        if (caption.isNotBlank()) {
            Text(caption, fontSize = 11.sp, color = UniforOutline)
        }
    }
}

@Composable
private fun BookDescriptionSection(
    book: Book,
    emprestimoLoading: Boolean,
    onSolicitarEmprestimo: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Descrição da Obra", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            book.sinopse ?: "Sem descrição disponível.",
            fontSize = 14.sp,
            color = UniforOutline,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSolicitarEmprestimo,
            enabled = !emprestimoLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
        ) {
            if (emprestimoLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Solicitar Empréstimo", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Exibido apenas quando mediaAvaliacao > 0 (há avaliações reais no backend)
@Composable
private fun RatingSection(book: Book) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Avaliação", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = UniforSurface,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = UniforTertiary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "%.1f".format(book.mediaAvaliacao),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = UniforPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "/ 5",
                    fontSize = 18.sp,
                    color = UniforOutline,
                    modifier = Modifier.align(Alignment.Bottom).padding(bottom = 4.dp)
                )
            }
        }
    }
}
