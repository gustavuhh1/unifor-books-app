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

private data class BookComment(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val timeAgo: String,
    val rating: Int,
    val text: String,
    val likes: Int,
    val isLiked: Boolean
)

private val mockComments = listOf(
    BookComment("1", "Beatriz Menezes", "Estudante", "Ontem", 5, "Leitura obrigatória para quem quer entender o futuro das bibliotecas digitais.", 24, true),
    BookComment("2", "Prof. Marcos André", "Docente", "Há 3 dias", 4, "O capítulo sobre ontologias é o ponto alto.", 12, false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    onBack: () -> Unit,
    onReportComment: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf(0) }
    val filters = listOf("Mais Relevantes", "Mais Recentes", "Mais Curtidos")

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
                    }
                },
                title = { Text("Detalhes do Livro", color = UniforPrimary, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
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
            item { BookDescriptionSection() }
            item {
                CommentsHeader(
                    selectedFilter = selectedFilter,
                    filters = filters,
                    onFilterSelected = { selectedFilter = it }
                )
            }
            items(mockComments) { comment ->
                CommentItem(comment = comment, onReportClick = { onReportComment(comment.id) })
            }
            item { ViewMoreButton() }
        }
    }
}

@Composable
private fun BookHeroSection() {
    // ... (conteúdo da seção omitido para brevidade)
}

@Composable
private fun BookDescriptionSection() {
    // ... (conteúdo da seção omitido para brevidade)
}

@Composable
private fun CommentsHeader(selectedFilter: Int, filters: List<String>, onFilterSelected: (Int) -> Unit) {
    // ... (conteúdo da seção omitido para brevidade)
}

@Composable
private fun CommentItem(comment: BookComment, onReportClick: () -> Unit) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(UniforSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = UniforPrimary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(comment.authorName, fontWeight = FontWeight.Bold, color = UniforPrimary)
                        Text("${comment.authorRole} • ${comment.timeAgo}", fontSize = 12.sp, color = UniforOutline)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(comment.rating) {
                        Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
                    }
                    IconButton(onClick = onReportClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Flag, "Denunciar", tint = UniforOutline.copy(alpha = 0.7f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(comment.text, fontSize = 14.sp, color = UniforOutline, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(14.dp))
            // ... (Ações de curtir e responder)
        }
    }
}

@Composable
private fun ViewMoreButton() {
    // ... (conteúdo da seção omitido para brevidade)
}
