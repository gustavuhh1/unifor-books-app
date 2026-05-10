package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.*
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

// --- Mock data ---

private data class BookComment(
    val authorName: String,
    val authorRole: String,
    val timeAgo: String,
    val rating: Int,
    val text: String,
    val likes: Int,
    val isLiked: Boolean
)

private val mockComments = listOf(
    BookComment(
        authorName = "Beatriz Menezes",
        authorRole = "Estudante de Engenharia",
        timeAgo = "Ontem",
        rating = 5,
        text = "Leitura obrigatória para quem quer entender o futuro das bibliotecas digitais. " +
                "A linguagem é técnica mas muito acessível. Recomendo fortemente para os alunos do primeiro semestre.",
        likes = 24,
        isLiked = true
    ),
    BookComment(
        authorName = "Prof. Marcos André",
        authorRole = "Docente",
        timeAgo = "Há 3 dias",
        rating = 4,
        text = "O capítulo sobre ontologias é o ponto alto. Embora o livro tenha sido escrito há dois anos, " +
                "os conceitos permanecem extremamente atuais dentro do nosso campus.",
        likes = 12,
        isLiked = false
    )
)

// --- Screen ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(onBack: () -> Unit) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf("Mais Relevantes", "Mais Recentes", "Mais Curtidos")

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = UniforPrimary
                        )
                    }
                },
                title = {
                    Text(
                        "UNIFOR",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = UniforPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(UniforSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = UniforPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = UniforBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item { BookHeroSection() }
            item { RatingQuickLookRow() }
            item { BookDescriptionSection() }
            item { ReadingAnalysisSection() }
            item {
                CommentsHeader(
                    selectedFilter = selectedFilter,
                    filters = filters,
                    onFilterSelected = { selectedFilter = it }
                )
            }
            items(mockComments) { comment -> CommentItem(comment) }
            item { ViewMoreButton() }
        }
    }
}

// --- Sections ---

@Composable
private fun BookHeroSection() {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 20.dp)
    ) {
        // Capa do livro (placeholder até integração com backend)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1A1A2E)),
            contentAlignment = Alignment.Center
        ) {
            // Badge DISPONÍVEL no canto superior esquerdo
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                Surface(
                    modifier = Modifier.padding(12.dp),
                    color = UniforSecondaryContainer,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        "DISPONÍVEL",
                        color = UniforSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Icon(
                Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "A Arquitetura da Informação Contemporânea",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = UniforPrimary,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Dr. Ricardo Vasconcelos",
            fontSize = 16.sp,
            color = UniforOutline,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RatingQuickLookRow() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        color = UniforSurfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estrelas + nota
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(4) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = UniforTertiaryFixed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.StarHalf,
                        contentDescription = null,
                        tint = UniforTertiaryFixed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "4.5 / 5.0",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = UniforOnSurface
                )
            }

            MetricDivider()

            Column {
                Text(
                    "AVALIAÇÕES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforOutline,
                    letterSpacing = 0.5.sp
                )
                Text("128", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
            }

            MetricDivider()

            Column {
                Text(
                    "PÁGINAS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforOutline,
                    letterSpacing = 0.5.sp
                )
                Text("432", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
            }
        }
    }
}

@Composable
private fun MetricDivider() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .width(1.dp)
            .height(40.dp)
            .background(UniforSurfaceContainerHigh)
    )
}

@Composable
private fun BookDescriptionSection() {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            "Descrição da Obra",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = UniforOnSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Uma exploração profunda sobre como os sistemas digitais moldam nossa percepção da " +
                    "realidade acadêmica. Este volume premiado pela UNIFOR Press oferece uma visão crítica " +
                    "sobre as estruturas de dados no contexto bibliográfico moderno, servindo como guia " +
                    "essencial para estudantes de Design e Ciência da Computação.",
            fontSize = 15.sp,
            color = UniforOutline,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Solicitar Empréstimo",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ReadingAnalysisSection() {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Análise de Leitura",
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card de distribuição de notas
            Surface(
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxHeight(),
                color = UniforSurfaceContainerLow,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                "4.5",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Black,
                                color = UniforPrimary,
                                lineHeight = 60.sp
                            )
                            Text(
                                "Média baseada em 128 leitores",
                                fontSize = 12.sp,
                                color = UniforOutline
                            )
                        }
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = UniforTertiaryFixed,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val ratingBars = listOf(
                        Triple(5, 0.75f, UniforSecondary),
                        Triple(4, 0.15f, UniforSecondary),
                        Triple(3, 0.08f, UniforTertiaryFixed),
                        Triple(2, 0.02f, Color(0xFFFFB3AC))
                    )
                    ratingBars.forEach { (star, progress, color) ->
                        RatingBar(star = star, progress = progress, barColor = color)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Card "Escolha da Equipe"
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                color = UniforTertiaryContainer,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = Color(0xFFFFDEA9),
                        modifier = Modifier.size(40.dp)
                    )
                    Column {
                        Text(
                            "Escolha da Equipe",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = UniforTertiaryFixed,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "\"Essencial para o currículo de Arquitetura de Dados da UNIFOR.\"",
                            fontSize = 12.sp,
                            color = Color(0xFFF3B01F).copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingBar(star: Int, progress: Float, barColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "$star",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = UniforOnSurface,
            modifier = Modifier.width(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(50.dp))
                    .background(barColor)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "${(progress * 100).toInt()}%",
            fontSize = 11.sp,
            color = UniforOutline,
            modifier = Modifier.width(30.dp)
        )
    }
}

@Composable
private fun CommentsHeader(
    selectedFilter: Int,
    filters: List<String>,
    onFilterSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 8.dp)
    ) {
        Text(
            "Comentários da Comunidade",
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEachIndexed { index, label ->
                FilterChip(
                    selected = selectedFilter == index,
                    onClick = { onFilterSelected(index) },
                    label = {
                        Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UniforPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = UniforSurfaceContainerHigh,
                        labelColor = UniforOutline
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == index,
                        borderColor = Color.Transparent,
                        selectedBorderColor = Color.Transparent
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CommentItem(comment: BookComment) {
    var isLiked by remember { mutableStateOf(comment.isLiked) }
    var likeCount by remember { mutableIntStateOf(comment.likes) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        color = UniforSurfaceContainerLowest,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: avatar + info + stars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(UniforSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = UniforPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            comment.authorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = UniforPrimary
                        )
                        Text(
                            "${comment.authorRole} • ${comment.timeAgo}",
                            fontSize = 11.sp,
                            color = UniforOutline
                        )
                    }
                }
                // Estrelas da avaliação
                Row {
                    repeat(comment.rating) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = UniforTertiaryFixed,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    repeat(5 - comment.rating) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = UniforSurfaceContainerHigh,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                comment.text,
                fontSize = 14.sp,
                color = UniforOutline,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Ações: curtir + responder
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = {
                        if (isLiked) {
                            isLiked = false
                            likeCount--
                        } else {
                            isLiked = true
                            likeCount++
                        }
                    },
                    color = if (isLiked) UniforSecondaryContainer else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    border = if (!isLiked) BorderStroke(1.dp, UniforSurfaceContainerHigh) else null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = "Curtir",
                            tint = if (isLiked) UniforSecondary else UniforOutline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "$likeCount",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isLiked) UniforSecondary else UniforOutline
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                TextButton(onClick = { }) {
                    Text(
                        "Responder",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = UniforOutline
                    )
                }
            }
        }
    }
}

@Composable
private fun ViewMoreButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = { },
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.5.dp, UniforSurfaceContainerHigh),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = UniforPrimary)
        ) {
            Text(
                "Ver mais 126 comentários",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
