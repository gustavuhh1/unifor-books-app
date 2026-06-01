package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unifor.booksapp.data.models.Book
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.CatalogFilter
import com.unifor.booksapp.ui.viewmodels.CatalogUiState
import com.unifor.booksapp.ui.viewmodels.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onBookClick: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    catalogViewModel: CatalogViewModel = viewModel()
) {
    val uiState by catalogViewModel.uiState.collectAsState()
    val query by catalogViewModel.query.collectAsState()
    val activeFilter by catalogViewModel.activeFilter.collectAsState()

    Scaffold(
        topBar = { CatalogTopBar(onBack = onBack, onNavigateToProfile = onNavigateToProfile) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CatalogSearchBar(
                query = query,
                onQueryChange = catalogViewModel::onQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            CatalogFilterRow(
                activeFilter = activeFilter,
                onFilterChange = catalogViewModel::onFilterChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is CatalogUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CatalogUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Erro ao carregar catálogo",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = catalogViewModel::fetchBooks) {
                                Text("Tentar novamente", color = UniforPrimary)
                            }
                        }
                    }
                }
                is CatalogUiState.Success -> {
                    if (state.books.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nenhum livro encontrado.", color = UniforOutline)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(state.books, key = { it.id }) { book ->
                                CatalogBookCard(book = book, onClick = { onBookClick(book.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogTopBar(onBack: () -> Unit, onNavigateToProfile: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
            }
        },
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, fontSize = 20.sp, color = UniforPrimary) },
        actions = {
            Box(
                modifier = Modifier
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
private fun CatalogSearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Pesquisar por título, autor ou ISBN", color = UniforOutline.copy(alpha = 0.7f)) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = UniforOutline) },
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = UniforSurfaceContainerHigh,
            unfocusedContainerColor = UniforSurfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = UniforPrimary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun CatalogFilterRow(
    activeFilter: CatalogFilter,
    onFilterChange: (CatalogFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        CatalogFilter.TODOS to "Todos",
        CatalogFilter.MAIOR_AVALIACAO to "Maior avaliação",
        CatalogFilter.DISPONIVEIS to "Disponíveis"
    )

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filters.forEach { (filter, label) ->
            val selected = activeFilter == filter
            Surface(
                onClick = { onFilterChange(filter) },
                shape = RoundedCornerShape(50.dp),
                color = if (selected) UniforPrimary else Color.Transparent,
                border = if (selected) null else ButtonDefaults.outlinedButtonBorder,
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun CatalogBookCard(book: Book, onClick: () -> Unit) {
    val available = book.exemplaresDisponiveis > 0

    Column(modifier = Modifier.clickable(onClick = onClick)) {
        // FIX: usa BookCoverImage com fallback de erro em vez de AsyncImage direto
        BookCoverImage(
            url = book.capaUrl,
            title = book.titulo,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.72f),
            corner = 16
        )

        Spacer(Modifier.height(8.dp))

        // Badge de disponibilidade
        Surface(
            color = if (available) Color(0xFFB8F5C8) else Color(0xFFE0E0E0),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text(
                if (available) "DISPONÍVEL" else "INDISPONÍVEL",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (available) Color(0xFF1A6B35) else Color(0xFF5A5A5A)
            )
        }

        Spacer(Modifier.height(6.dp))
        Text(
            book.titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            book.autor,
            fontSize = 12.sp,
            color = UniforOutline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        StarRatingRow(rating = book.mediaAvaliacao.toFloat())
    }
}

@Composable
private fun StarRatingRow(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.3f
        val emptyStars = (5 - fullStars - if (hasHalf) 1 else 0).coerceAtLeast(0)
        repeat(fullStars) {
            Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
        }
        if (hasHalf) {
            Icon(Icons.Default.StarHalf, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
        }
        repeat(emptyStars) {
            Icon(Icons.Outlined.StarOutline, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
        }
        Spacer(Modifier.width(4.dp))
        Text("%.1f".format(rating), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = UniforOutline)
    }
}
