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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unifor.booksapp.data.models.Avaliacao
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.AvaliacoesUiState
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
    val avaliacoesState by bookDetailViewModel.avaliacoesState.collectAsState()

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
                        avaliacoesState = avaliacoesState,
                        emprestimoLoading = emprestimoState is EmprestimoUiState.Loading,
                        onSolicitarEmprestimo = { bookDetailViewModel.solicitarEmprestimo(state.book) },
                        onReportComment = onReportComment
                    )
                }
            }
        }
    }
}

@Composable
private fun BookDetailContent(
    book: Book,
    avaliacoesState: AvaliacoesUiState,
    emprestimoLoading: Boolean,
    onSolicitarEmprestimo: () -> Unit,
    onReportComment: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Capa do livro ─────────────────────────────────────
        // FIX: BookCoverImage reutilizável — trata capaUrl nula e erro de carregamento
        BookCoverImage(
            capaUrl = book.capaUrl,
            titulo = book.titulo,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            cornerRadius = 0
        )

        // ── Conteúdo principal ────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            Spacer(modifier = Modifier.height(20.dp))

            // Badge disponibilidade
            AvailabilityBadge(available = book.exemplaresDisponiveis > 0)

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = book.titulo,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Autor
            Text(
                text = book.autor,
                fontSize = 15.sp,
                color = UniforOutline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card de estatísticas (nota, avaliações, exemplares, páginas)
            StatsCard(book = book, avaliacoesState = avaliacoesState)

            Spacer(modifier = Modifier.height(24.dp))

            // Descrição
            Text(
                text = "Descrição da Obra",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = UniforPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.sinopse ?: "Sem descrição disponível.",
                fontSize = 15.sp,
                color = Color(0xFF444444),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Informações do livro (categoria, editora, ano, idioma)
            BookInfoSection(book = book)

            Spacer(modifier = Modifier.height(24.dp))

            // Botão empréstimo
            Button(
                onClick = onSolicitarEmprestimo,
                enabled = !emprestimoLoading && book.exemplaresDisponiveis > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                if (emprestimoLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Solicitar Empréstimo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ── Análise de Leitura ────────────────────────────────
        HorizontalDivider(color = UniforOutline.copy(alpha = 0.1f))
        Spacer(modifier = Modifier.height(24.dp))

        when (val state = avaliacoesState) {
            is AvaliacoesUiState.Success -> {
                if (state.total > 0) {
                    RatingAnalysisSection(
                        media = state.media,
                        total = state.total,
                        avaliacoes = state.avaliacoes
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    CommentsSection(
                        avaliacoes = state.avaliacoes,
                        total = state.total,
                        onReportComment = onReportComment
                    )
                }
            }
            is AvaliacoesUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
            else -> Unit
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun AvailabilityBadge(available: Boolean) {
    Surface(
        color = if (available) Color(0xFFB8F5C8) else Color(0xFFE0E0E0),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = if (available) "DISPONÍVEL" else "INDISPONÍVEL",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (available) Color(0xFF1A6B35) else Color(0xFF5A5A5A)
        )
    }
}

@Composable
private fun StatsCard(book: Book, avaliacoesState: AvaliacoesUiState) {
    val totalAvaliacoes = when (avaliacoesState) {
        is AvaliacoesUiState.Success -> avaliacoesState.total
        else -> null
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF3F4F5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estrelas + nota média
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val rating = book.mediaAvaliacao.toFloat()
                    val fullStars = rating.toInt()
                    val hasHalf = (rating - fullStars) >= 0.3f
                    val emptyStars = (5 - fullStars - if (hasHalf) 1 else 0).coerceAtLeast(0)
                    repeat(fullStars) {
                        Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(15.dp))
                    }
                    if (hasHalf) {
                        Icon(Icons.Default.StarHalf, null, tint = UniforTertiaryFixed, modifier = Modifier.size(15.dp))
                    }
                    repeat(emptyStars) {
                        Icon(Icons.Default.StarBorder, null, tint = UniforTertiaryFixed, modifier = Modifier.size(15.dp))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "%.1f / 5.0".format(book.mediaAvaliacao),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = UniforOutline
                )
            }

            VerticalDivider(modifier = Modifier.height(36.dp), color = UniforOutline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.width(16.dp))

            // Avaliações (popularidade)
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "AVALIAÇÕES",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforOutline,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = totalAvaliacoes?.toString() ?: "—",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = UniforPrimary
                )
            }

            // Exemplares no acervo
            if (book.totalExemplares > 0) {
                Spacer(modifier = Modifier.width(16.dp))
                VerticalDivider(modifier = Modifier.height(36.dp), color = UniforOutline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "EXEMPLARES",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = UniforOutline,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${book.exemplaresDisponiveis}/${book.totalExemplares}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = UniforPrimary
                    )
                }
            }

            if (book.paginas != null) {
                Spacer(modifier = Modifier.width(16.dp))
                VerticalDivider(modifier = Modifier.height(36.dp), color = UniforOutline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "PÁGINAS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = UniforOutline,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = book.paginas.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = UniforPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun BookInfoSection(book: Book) {
    val items = buildList {
        add(Triple(Icons.Default.Category, "Categoria", book.categoria))
        book.editora?.let { add(Triple(Icons.Default.Business, "Editora", it)) }
        book.anoPublicacao?.let { add(Triple(Icons.Default.DateRange, "Publicação", it.toString())) }
        book.idioma?.let { add(Triple(Icons.Default.Language, "Idioma", it)) }
    }

    if (items.isEmpty()) return

    Text(
        text = "Informações",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = UniforPrimary
    )
    Spacer(modifier = Modifier.height(10.dp))

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF3F4F5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            items.forEachIndexed { index, (icon, label, value) ->
                BookInfoRow(icon = icon, label = label, value = value)
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = UniforOutline.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = UniforOutline, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 13.sp, color = UniforOutline, modifier = Modifier.width(80.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RatingAnalysisSection(media: Double, total: Int, avaliacoes: List<Avaliacao>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Análise de Leitura",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.1f".format(media),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = UniforPrimary
                )
                Text(
                    text = "Média baseada em\n$total leitores",
                    fontSize = 11.sp,
                    color = UniforOutline,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val countsByStars = (5 downTo 1).map { star ->
                    star to avaliacoes.count { it.nota == star }
                }
                countsByStars.forEach { (star, count) ->
                    val fraction = if (total > 0) count.toFloat() / total else 0f
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$star",
                            fontSize = 11.sp,
                            color = UniforOutline,
                            modifier = Modifier.width(10.dp),
                            textAlign = TextAlign.End
                        )
                        Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(11.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50.dp)),
                            color = UniforTertiaryFixed,
                            trackColor = UniforOutline.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentsSection(
    avaliacoes: List<Avaliacao>,
    total: Int,
    onReportComment: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Comentários da Comunidade",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        avaliacoes.filter { !it.comentario.isNullOrBlank() }.forEach { avaliacao ->
            CommentCard(avaliacao = avaliacao, onReport = { onReportComment(avaliacao.id) })
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (total > avaliacoes.size) {
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Ver mais ${total - avaliacoes.size} comentários",
                    color = UniforPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CommentCard(avaliacao: Avaliacao, onReport: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF3F4F5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val nome = avaliacao.usuario?.nome ?: "U"
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(UniforPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nome.firstOrNull()?.uppercase() ?: "U",
                        fontWeight = FontWeight.Bold,
                        color = UniforPrimary,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UniforPrimary)
                    val perfil = avaliacao.usuario?.perfil
                    if (!perfil.isNullOrBlank()) {
                        Surface(
                            color = UniforPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = perfil,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = UniforPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Icon(
                    Icons.Default.Flag,
                    contentDescription = "Reportar",
                    tint = UniforOutline.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onReport() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(avaliacao.nota.coerceIn(0, 5)) {
                    Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
                }
                repeat((5 - avaliacao.nota).coerceAtLeast(0)) {
                    Icon(Icons.Default.StarBorder, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = avaliacao.comentario ?: "",
                fontSize = 14.sp,
                color = Color(0xFF444444),
                lineHeight = 20.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onReport,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.height(32.dp)
            ) {
                Text("Reportar", fontSize = 12.sp, color = UniforOutline, fontWeight = FontWeight.Medium)
            }
        }
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
