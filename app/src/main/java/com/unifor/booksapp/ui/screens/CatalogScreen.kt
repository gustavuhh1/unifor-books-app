package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
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
import com.unifor.booksapp.ui.theme.*

// ─── Modelos de dados locais ─────────────────────────────────────────────────

private enum class CatalogFilter(val label: String) {
    ALL("Todos"),
    TOP_RATED("Maior avaliação"),
    AVAILABLE("Disponíveis")
}

private data class CatalogBook(
    val id: String,
    val title: String,
    val author: String,
    val rating: Float,
    val available: Boolean,
    val coverColor: Color
)

// Lista vazia: será preenchida pelo banco de dados.
private val sampleBooks: List<CatalogBook> = emptyList()

// ─── Tela principal ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onBookClick: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf(CatalogFilter.ALL) }

    val filteredBooks = remember(query, activeFilter) {
        sampleBooks
            .filter { book ->
                if (query.isBlank()) true
                else book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true)
            }
            .filter { book ->
                when (activeFilter) {
                    CatalogFilter.ALL -> true
                    CatalogFilter.TOP_RATED -> book.rating >= 4.5f
                    CatalogFilter.AVAILABLE -> book.available
                }
            }
    }

    Scaffold(
        topBar = {
            CatalogTopBar(onBack = onBack)
        },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Barra de busca ────────────────────────────────────────────
            CatalogSearchBar(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // ── Filtros de chip ───────────────────────────────────────────
            CatalogFilterChips(
                activeFilter = activeFilter,
                onFilterChange = { activeFilter = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Grade de livros ───────────────────────────────────────────
            if (filteredBooks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Sem texto: aguardando dados do banco.
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBooks, key = { it.id }) { book ->
                        CatalogBookCard(book = book, onClick = { onBookClick(book.id) })
                    }
                    // Espaço extra no final para não ficar sob a BottomNav
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ─── TopBar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogTopBar(onBack: () -> Unit) {
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
                "Unifor Books",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = UniforPrimary
            )
        },
        actions = {
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

// ─── Search bar ──────────────────────────────────────────────────────────────

@Composable
private fun CatalogSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(UniforSurfaceContainerHigh, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = UniforOutline)
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    "Pesquisar por título, autor ou ISBN",
                    color = UniforOutline.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = UniforPrimary
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

// ─── Filter chips ─────────────────────────────────────────────────────────────

@Composable
private fun CatalogFilterChips(
    activeFilter: CatalogFilter,
    onFilterChange: (CatalogFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CatalogFilter.entries.forEach { filter ->
            val selected = activeFilter == filter
            FilterChip(
                selected = selected,
                onClick = { onFilterChange(filter) },
                label = {
                    Text(
                        filter.label,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UniforPrimary,
                    selectedLabelColor = Color.White,
                    containerColor = Color.Transparent,
                    labelColor = UniforOutline
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = UniforOutline.copy(alpha = 0.3f),
                    selectedBorderColor = UniforPrimary
                )
            )
        }
    }
}

// ─── Book card ────────────────────────────────────────────────────────────────

@Composable
private fun CatalogBookCard(
    book: CatalogBook,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = UniforSurface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Capa do livro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.72f)
                    .background(book.coverColor)
            ) {
                // Badge disponibilidade
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart),
                    color = if (book.available) UniforSecondaryContainer else Color(0xFFFFDAD6),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (book.available) "DISPONÍVEL" else "INDISPONÍVEL",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (book.available) UniforOnSecondaryContainer else Color(0xFFBA1A1A)
                    )
                }
            }

            // Informações
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = UniforOnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.author,
                    fontSize = 11.sp,
                    color = UniforOutline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                StarRatingRow(rating = book.rating)
            }
        }
    }
}

// ─── Estrelas ─────────────────────────────────────────────────────────────────

@Composable
private fun StarRatingRow(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.3f
        val emptyStars = 5 - fullStars - (if (hasHalf) 1 else 0)

        repeat(fullStars) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = UniforTertiaryFixed,
                modifier = Modifier.size(14.dp)
            )
        }
        if (hasHalf) {
            Icon(
                Icons.Default.StarHalf,
                contentDescription = null,
                tint = UniforTertiaryFixed,
                modifier = Modifier.size(14.dp)
            )
        }
        repeat(emptyStars) {
            Icon(
                Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = UniforTertiaryFixed,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = UniforOutline
        )
    }
}
