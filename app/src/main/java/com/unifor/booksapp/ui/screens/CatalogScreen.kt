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
import com.unifor.booksapp.ui.theme.*

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

private val sampleBooks: List<CatalogBook> = listOf(
    CatalogBook("1", "O Guia do Mochileiro das Galáxias", "Douglas Adams", 4.5f, true, Color(0xFFF39C12)),
    CatalogBook("2", "Neuromancer", "William Gibson", 4.8f, false, Color(0xFF9B59B6)),
    CatalogBook("3", "Duna", "Frank Herbert", 4.9f, true, Color(0xFFE67E22)),
    CatalogBook("4", "Fundação", "Isaac Asimov", 4.7f, true, Color(0xFF3498DB))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onBookClick: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToProfile: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf(CatalogFilter.ALL) }

    val filteredBooks = remember(query, activeFilter) {
        sampleBooks.filter { book ->
            val queryMatch = query.isBlank() ||
                    book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true)

            val filterMatch = when (activeFilter) {
                CatalogFilter.ALL -> true
                CatalogFilter.TOP_RATED -> book.rating >= 4.5f
                CatalogFilter.AVAILABLE -> book.available
            }

            queryMatch && filterMatch
        }
    }

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
                onQueryChange = { query = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            CatalogFilterChips(
                activeFilter = activeFilter,
                onFilterChange = { activeFilter = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredBooks.isEmpty()) {
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
                    items(filteredBooks, key = { it.id }) { book ->
                        CatalogBookCard(book = book, onClick = { onBookClick(book.id) })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogFilterChips(activeFilter: CatalogFilter, onFilterChange: (CatalogFilter) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CatalogFilter.values().forEach { filter ->
            val selected = activeFilter == filter
            FilterChip(
                selected = selected,
                onClick = { onFilterChange(filter) },
                label = { Text(filter.label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UniforPrimary,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = UniforOutline.copy(alpha = 0.3f),
                    selectedBorderColor = UniforPrimary,
                    disabledBorderColor = Color.Transparent,
                    disabledSelectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun CatalogBookCard(book: CatalogBook, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(16.dp), color = UniforSurface, shadowElevation = 2.dp) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.72f).background(book.coverColor)) {
                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.BottomStart),
                    color = if (book.available) UniforSecondaryContainer else Color(0xFFFFDAD6),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        if (book.available) "DISPONÍVEL" else "INDISPONÍVEL",
                        Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (book.available) UniforOnSecondaryContainer else Color(0xFFBA1A1A)
                    )
                }
            }
            Column(Modifier.padding(12.dp)) {
                Text(book.title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(book.author, fontSize = 12.sp, color = UniforOutline, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                StarRatingRow(rating = book.rating)
            }
        }
    }
}

@Composable
private fun StarRatingRow(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.3f
        val emptyStars = 5 - fullStars - (if (hasHalf) 1 else 0)
        repeat(fullStars) { Icon(Icons.Default.Star, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp)) }
        if (hasHalf) Icon(Icons.Default.StarHalf, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp))
        repeat(emptyStars) { Icon(Icons.Outlined.StarOutline, null, tint = UniforTertiaryFixed, modifier = Modifier.size(14.dp)) }
        Spacer(Modifier.width(4.dp))
        Text(rating.toString(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = UniforOutline)
    }
}
