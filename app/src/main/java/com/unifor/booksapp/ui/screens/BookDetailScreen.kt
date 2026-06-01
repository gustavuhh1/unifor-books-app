package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    onNavigateToLoanApproved: (title: String, author: String, deadline: String) -> Unit = { _, _, _ -> },
    onNavigateToLoanUnavailable: (title: String, author: String) -> Unit = { _, _ -> },
    onNavigateToLoanQueue: (position: Int, title: String, author: String) -> Unit = { _, _, _ -> },
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
            is EmprestimoUiState.Aprovado -> {
                bookDetailViewModel.resetEmprestimoState()
                onNavigateToLoanApproved(state.bookTitle, state.bookAuthor, state.prazoRetirada ?: "")
            }
            is EmprestimoUiState.NaFila -> {
                bookDetailViewModel.resetEmprestimoState()
                onNavigateToLoanQueue(state.posicao, state.bookTitle, state.bookAuthor)
            }
            is EmprestimoUiState.Indisponivel -> {
                bookDetailViewModel.resetEmprestimoState()
                onNavigateToLoanUnavailable(state.bookTitle, state.bookAuthor)
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
                        onReportComment = onReportComment,
                        onLoadMoreComments = { bookDetailViewModel.loadMoreAvaliacoes() }
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
    onReportComment: (String) -> Unit,
    onLoadMoreComments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Cover + info block (matches React inline-grid section)
        Column {
            BookCoverSection(url = book.capaUrl, title = book.titulo)
            Spacer(modifier = Modifier.height(48.dp))
            BookInfoBlock(
                book = book,
                avaliacoesState = avaliacoesState,
                emprestimoLoading = emprestimoLoading,
                onSolicitarEmprestimo = onSolicitarEmprestimo
            )
        }

        // Análise de Leitura + Comentários
        when (val state = avaliacoesState) {
            is AvaliacoesUiState.Success -> {
                if (state.total > 0) {
                    RatingAnalysisSection(
                        media = state.media,
                        total = state.total,
                        avaliacoes = state.avaliacoes
                    )
                    CommentsSection(
                        avaliacoes = state.avaliacoes,
                        total = state.total,
                        onReportComment = onReportComment,
                        onLoadMore = onLoadMoreComments
                    )
                }
            }
            is AvaliacoesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun BookCoverSection(url: String?, title: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Decorative circle behind cover (React: absolute -left-4 -top-4, size 352dp, opacity 50% #00346F/5%)
        Box(
            modifier = Modifier
                .offset(x = (-16).dp, y = (-16).dp)
                .size(352.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(UniforPrimary.copy(alpha = 0.05f))
        )
        BookCoverImage(
            url = url,
            title = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(8.dp), clip = false)
                .clip(RoundedCornerShape(8.dp)),
            corner = 8
        )
    }
}

@Composable
private fun BookInfoBlock(
    book: Book,
    avaliacoesState: AvaliacoesUiState,
    emprestimoLoading: Boolean,
    onSolicitarEmprestimo: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
        // Badge + title + author (gap-2 = 8dp, title has pt-2 extra)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AvailabilityBadge(available = book.exemplaresDisponiveis > 0)
            Text(
                text = book.titulo,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = UniforPrimary,
                lineHeight = 45.sp
            )
            Text(
                text = book.autor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424751)
            )
        }

        StatsCard(book = book, avaliacoesState = avaliacoesState)

        // Description (gap-4 = 16dp, pb-4 = 16dp bottom)
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Descrição do Obra",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191C1D)
            )
            Text(
                text = book.sinopse ?: "Sem descrição disponível.",
                fontSize = 18.sp,
                color = Color(0xFF424751),
                lineHeight = 29.sp
            )
        }

        // Solicitar Empréstimo button (py-4 = ~60dp total height, rounded-lg = 8dp)
        Button(
            onClick = onSolicitarEmprestimo,
            enabled = !emprestimoLoading && book.exemplaresDisponiveis > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(8.dp),
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
                    text = "Solicitar Empréstimo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun AvailabilityBadge(available: Boolean) {
    // rounded-xl = 12dp, py-1 px-3 = 4dp 12dp
    Surface(
        color = if (available) UniforSecondaryContainer else Color(0xFFE0E0E0),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = if (available) "DISPONÍVEL" else "INDISPONÍVEL",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (available) UniforOnSecondaryContainer else Color(0xFF5A5A5A)
        )
    }
}

@Composable
private fun StatsCard(book: Book, avaliacoesState: AvaliacoesUiState) {
    val totalAvaliacoes = when (avaliacoesState) {
        is AvaliacoesUiState.Success -> avaliacoesState.total
        else -> null
    }

    // rounded-2xl = 16dp, py-[22px] px-4, gap-5 = 20dp between columns
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stars (20dp each) + "4.5 / 5.0" below
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val rating = book.mediaAvaliacao.toFloat()
                    val fullStars = rating.toInt()
                    val hasHalf = (rating - fullStars) >= 0.3f
                    val emptyStars = (5 - fullStars - if (hasHalf) 1 else 0).coerceAtLeast(0)
                    repeat(fullStars) {
                        Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(20.dp))
                    }
                    if (hasHalf) {
                        Icon(Icons.AutoMirrored.Filled.StarHalf, null, tint = UniforTertiaryFixed, modifier = Modifier.size(20.dp))
                    }
                    repeat(emptyStars) {
                        Icon(Icons.Default.StarBorder, null, tint = UniforTertiaryFixed, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "%.1f / 5.0".format(book.mediaAvaliacao),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1D)
                )
            }

            // AVALIAÇÕES label + count
            Column {
                Text(
                    text = "AVALIAÇÕES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424751),
                    letterSpacing = 0.6.sp
                )
                Text(
                    text = totalAvaliacoes?.toString() ?: "—",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforPrimary
                )
            }

            // PÁGINAS label + count (only if available)
            if (book.paginas != null) {
                Column {
                    Text(
                        text = "PÁGINAS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424751),
                        letterSpacing = 0.6.sp
                    )
                    Text(
                        text = book.paginas.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = UniforPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingAnalysisSection(media: Double, total: Int, avaliacoes: List<Avaliacao>) {
    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(
            text = "Análise de Leitura",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = UniforPrimary
        )

        // Card: rounded-3xl = 24dp, p-8 = 32dp, gap-8 = 32dp
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = UniforSurfaceContainerLow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Top row: big score left, verified badge right (items-end = bottom-aligned)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "%.1f".format(media),
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Black,
                            color = UniforPrimary,
                            lineHeight = 60.sp
                        )
                        Text(
                            text = "Média baseada em $total leitores",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF424751)
                        )
                    }
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = UniforTertiaryFixed,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Rating bars: 5, 4, 3, 2 (gap-3 = 12dp, gap-4 = 16dp inside row)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(5, 4, 3, 2).forEach { star ->
                        val count = avaliacoes.count { it.nota == star }
                        val fraction = if (total > 0) count.toFloat() / total else 0f
                        val percentage = (fraction * 100).toInt()
                        val barColor = when (star) {
                            5, 4 -> UniforSecondary       // #006D35 green
                            3 -> UniforTertiaryFixed       // #FEBB2B yellow
                            else -> UniforErrorContainer   // #FFDAD6 light pink
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "$star",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF191C1D),
                                modifier = Modifier.width(16.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction)
                                        .background(barColor)
                                )
                            }
                            Text(
                                text = "$percentage%",
                                fontSize = 12.sp,
                                color = Color(0xFF424751),
                                modifier = Modifier.width(32.dp)
                            )
                        }
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
    onReportComment: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(
            text = "Comentários da Comunidade",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = UniforPrimary
        )

        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            avaliacoes.filter { !it.comentario.isNullOrBlank() }.forEach { avaliacao ->
                CommentCard(
                    avaliacao = avaliacao,
                    onReport = { onReportComment(avaliacao.id) }
                )
            }

            if (total > avaliacoes.size) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // "Ver mais" pill button: rounded-xl = 12dp, py-3 px-8 = 12dp 32dp
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = UniforSurfaceContainerHigh,
                        onClick = onLoadMore
                    ) {
                        Text(
                            text = "Ver mais ${total - avaliacoes.size} comentários",
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 32.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentCard(avaliacao: Avaliacao, onReport: () -> Unit) {
    var liked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(0) }

    // rounded-3xl = 24dp, p-8 = 32dp, gap-6 = 24dp
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header: [avatar + name/stars/date] justify-between + flag icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circle (46dp, #EDEEEF bg)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDEEEF)),
                        contentAlignment = Alignment.Center
                    ) {
                        val nome = avaliacao.usuario?.nome ?: "U"
                        Text(
                            text = nome.firstOrNull()?.uppercase() ?: "U",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforPrimary
                        )
                    }

                    // Name + 5 stars (12dp) + date (gap-[3px] = 3dp)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = avaliacao.usuario?.nome ?: "Usuário",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforPrimary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(avaliacao.nota.coerceIn(0, 5)) {
                                Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(12.dp))
                            }
                            repeat((5 - avaliacao.nota).coerceAtLeast(0)) {
                                Icon(Icons.Default.StarBorder, null, tint = UniforTertiaryFixed, modifier = Modifier.size(12.dp))
                            }
                        }
                        if (avaliacao.criadoEm.isNotBlank()) {
                            Text(
                                text = "• ${formatRelativeDate(avaliacao.criadoEm)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424751)
                            )
                        }
                    }
                }

                // Flag/report icon (24dp, #00346F, p-[5px] = 5dp padding)
                Icon(
                    Icons.Default.Flag,
                    contentDescription = "Reportar",
                    tint = UniforPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(5.dp)
                        .clickable { onReport() }
                )
            }

            // Comment text: text-base = 16sp, leading-[26px]
            Text(
                text = avaliacao.comentario ?: "",
                fontSize = 16.sp,
                color = Color(0xFF424751),
                lineHeight = 26.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            // Action row: like button + "Responder" (gap-4 = 16dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like button: 75dp x 36dp, rounded-lg = 8dp
                // Unliked: outlined border #B4B4B4, white bg
                // Liked: #8AFAA7 bg, no border
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (liked) UniforSecondaryContainer else Color.White,
                    border = if (!liked) BorderStroke(1.dp, Color(0xFFB4B4B4)) else null,
                    onClick = {
                        liked = !liked
                        likeCount += if (liked) 1 else -1
                    },
                    modifier = Modifier
                        .width(75.dp)
                        .height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = if (liked) UniforOnSecondaryContainer else Color(0xFFADADAD),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "$likeCount",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (liked) UniforOnSecondaryContainer else Color(0xFF444751)
                        )
                    }
                }

                // "Responder" text-only button
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Responder",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424751)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    // h-[72px] ≈ 72dp, bg white/80%, shadow
    TopAppBar(
        title = {
            Text(
                "Unifor Books",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = UniforPrimary
            )
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBack() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
            }
        },
        actions = {
            // Avatar: 45dp circle, #EDEEEF bg
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDEEEF))
                    .clickable { onNavigateToProfile() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = UniforPrimary, modifier = Modifier.size(28.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        modifier = Modifier.shadow(elevation = 4.dp)
    )
}

private fun formatRelativeDate(isoDate: String): String {
    if (isoDate.isBlank()) return ""
    return try {
        val instant = java.time.Instant.parse(isoDate)
        val diffSeconds = java.time.Duration.between(instant, java.time.Instant.now()).seconds
        when {
            diffSeconds < 60       -> "Agora"
            diffSeconds < 3600     -> "Há ${diffSeconds / 60} min"
            diffSeconds < 86400    -> "Há ${diffSeconds / 3600}h"
            diffSeconds < 2592000  -> "Há ${diffSeconds / 86400} dias"
            diffSeconds < 31536000 -> "Há ${diffSeconds / 2592000} meses"
            else                   -> "Há ${diffSeconds / 31536000} anos"
        }
    } catch (_: Exception) {
        isoDate.take(10)
    }
}
