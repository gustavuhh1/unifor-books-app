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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

private data class BookDetailsData(
    val title: String,
    val author: String,
    val description: String,
    val rating: Float,
    val reviewCount: Int,
    val pages: Int,
    val isAvailable: Boolean,
    val analysis: ReadingAnalysis,
    val comments: List<BookComment>
)

private data class ReadingAnalysis(
    val averageRating: Float,
    val totalRatings: Int,
    val ratingsDistribution: Map<Int, Float>
)

private data class BookComment(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    val rating: Int,
    val text: String,
    val likes: Int,
    val isLiked: Boolean
)

private val mockBookDetails = BookDetailsData(
    title = "A Arquitetura da Informação Contemporânea",
    author = "Dr. Ricardo Vasconcelos",
    description = "Uma exploração profunda sobre como os sistemas digitais moldam nossa percepção da realidade acadêmica. Este volume premiado pela UNIFOR Press oferece uma visão crítica sobre as estruturas de dados no contexto bibliográfico moderno, servindo como guia essencial para estudantes de Design e Ciência da Computação.",
    rating = 4.5f,
    reviewCount = 126,
    pages = 432,
    isAvailable = true,
    analysis = ReadingAnalysis(
        averageRating = 4.5f,
        totalRatings = 126,
        ratingsDistribution = mapOf(5 to 0.7f, 4 to 0.15f, 3 to 0.1f, 2 to 0.05f, 1 to 0f)
    ),
    comments = listOf(
        BookComment("1", "Beatriz Menezes", "há 3 dias", 5, "Leitura obrigatória para quem quer entender o futuro das bibliotecas digitais. A linguagem é técnica mas muito acessível. Recomendo fortemente para os alunos do primeiro semestre.", 24, false),
        BookComment("2", "Rodrigo Moraes", "há 8 dias", 5, "Leitura obrigatória para quem quer entender o futuro das bibliotecas digitais. A linguagem é técnica mas muito acessível. Recomendo fortemente para os alunos do primeiro semestre.", 35, true),
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    onBack: () -> Unit,
    onReportComment: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit,
    // Solicitar empréstimo → sempre vai para a fila (backend decide aprovação)
    onNavigateToLoanQueue: () -> Unit,
    // Telas de decisão do admin (acessadas via Dev Menu ou notificação futura)
    onNavigateToLoanApproved: () -> Unit = {},
    onNavigateToLoanUnavailable: () -> Unit = {}
) {
    val book = mockBookDetails

    Scaffold(
        topBar = { BookDetailTopBar(onBack = onBack, onNavigateToProfile = onNavigateToProfile) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            BookCoverSection()
            Spacer(modifier = Modifier.height(16.dp))
            BookInfoSection(book)
            Spacer(modifier = Modifier.height(24.dp))
            BookDescriptionSection(book, onNavigateToLoanQueue, onNavigateToLoanUnavailable)
            Spacer(modifier = Modifier.height(24.dp))
            ReadingAnalysisSection(book.analysis)
            Spacer(modifier = Modifier.height(24.dp))
            CommunityCommentsSection(book.comments, onReportComment)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary) } },
        actions = {
            Box(
                Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(UniforSurfaceContainerHigh)
                    .clickable { onNavigateToProfile() },
                contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

@Composable
private fun BookCoverSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "'A ARQUITETURA DA INFORMAÇÃO'",
                color = UniforOutline,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(10.dp))
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
                        "A Arquitetura\nda Informação\nContemporânea",
                        color = UniforPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "DR. RICARDO VASCONCELOS",
                color = UniforOutline,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun BookInfoSection(book: BookDetailsData) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        if (book.isAvailable) {
            Surface(
                color = UniforSuccessContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    "DISPONÍVEL",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = UniforOnSecondaryContainer,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(book.title, fontSize = 28.sp, fontWeight = FontWeight.Black, color = UniforPrimary, lineHeight = 34.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(book.author, fontSize = 16.sp, color = UniforOutline)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            RatingItem(value = book.rating.toString(), label = "AVALIAÇÕES", caption = "${book.reviewCount} Leitores", hasStars = true)
            RatingItem(value = book.pages.toString(), label = "PÁGINAS", caption = "4-5h de leitura")
        }
    }
}

@Composable
private fun RatingItem(value: String, label: String, caption: String, hasStars: Boolean = false) {
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
        Text(caption, fontSize = 11.sp, color = UniforOutline)
    }
}

@Composable
private fun BookDescriptionSection(
    book: BookDetailsData,
    onNavigateToLoanQueue: () -> Unit,
    onNavigateToLoanUnavailable: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Descrição da Obra", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(book.description, fontSize = 14.sp, color = UniforOutline, lineHeight = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Disponível → entra na fila aguardando aprovação do admin
                // Indisponível → exibe tela de livro indisponível
                if (book.isAvailable) {
                    onNavigateToLoanQueue()
                } else {
                    onNavigateToLoanUnavailable()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
        ) {
            Text("Solicitar Empréstimo", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ReadingAnalysisSection(analysis: ReadingAnalysis) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Análise de Leitura", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = UniforSurface,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(90.dp)) {
                    Text(analysis.averageRating.toString(), fontSize = 36.sp, fontWeight = FontWeight.Black, color = UniforPrimary)
                    Text("Média baseada em ${analysis.totalRatings} leitores", fontSize = 11.sp, color = UniforOutline, textAlign = TextAlign.Center, lineHeight = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(Icons.Default.CheckCircle, null, tint = UniforWarning, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    analysis.ratingsDistribution.forEach { (stars, percentage) ->
                        RatingBar(stars = stars, percentage = percentage)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingBar(stars: Int, percentage: Float) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(stars.toString(), fontSize = 12.sp, color = UniforOutline)
        Icon(Icons.Default.Star, null, tint = UniforTertiary, modifier = Modifier.size(12.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.weight(1f).height(8.dp).clip(CircleShape),
            color = if (stars >= 4) UniforSuccess else if (stars == 3) UniforWarning else UniforError,
            trackColor = UniforSurfaceContainerHigh
        )
        Text("${(percentage * 100).toInt()}%", fontSize = 12.sp, color = UniforOutline, modifier = Modifier.width(30.dp), textAlign = TextAlign.End)
    }
}

@Composable
private fun CommunityCommentsSection(comments: List<BookComment>, onReportClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
        Text("Comentários da Comunidade", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        comments.forEach { comment ->
            CommentCard(comment = comment, onReportClick = { onReportClick(comment.id) })
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, UniforOutline.copy(alpha = 0.3f))
        ) {
            Text("Ver mais ${mockBookDetails.reviewCount} comentários", color = UniforPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CommentCard(comment: BookComment, onReportClick: () -> Unit) {
    var isLiked by remember { mutableStateOf(comment.isLiked) }
    var likeCount by remember { mutableIntStateOf(comment.likes) }

    Surface(shape = RoundedCornerShape(16.dp), color = UniforSurface, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(UniforPrimary.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, tint = UniforPrimary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(comment.authorName, fontWeight = FontWeight.Bold, color = UniforPrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { Icon(Icons.Default.Star, null, tint = UniforTertiary, modifier = Modifier.size(12.dp)) }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("• ${comment.timeAgo}", fontSize = 11.sp, color = UniforOutline)
                        }
                    }
                }
                IconButton(onClick = onReportClick, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Outlined.Flag, "Denunciar", tint = UniforOutline.copy(alpha = 0.7f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(comment.text, fontSize = 14.sp, color = UniforOutline, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { isLiked = !isLiked; if (isLiked) likeCount++ else likeCount-- },
                    border = BorderStroke(1.dp, if (isLiked) UniforSuccess else UniforOutline.copy(alpha = 0.3f)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isLiked) UniforSuccessContainer else Color.Transparent)
                ) {
                    Icon(
                        if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        modifier = Modifier.size(16.dp),
                        tint = if (isLiked) UniforOnSecondaryContainer else UniforOutline
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(likeCount.toString(), fontWeight = FontWeight.Bold, color = if (isLiked) UniforOnSecondaryContainer else UniforOutline)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { }) {
                    Text("Responder", fontWeight = FontWeight.Bold, color = UniforOutline)
                }
            }
        }
    }
}
