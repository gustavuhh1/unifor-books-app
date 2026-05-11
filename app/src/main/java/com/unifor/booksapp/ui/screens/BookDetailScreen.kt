package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.UniforPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Livro") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Página de Detalhes do Livro", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    // Conteúdo dos detalhes do livro aqui...
                }
            }
        }
    }
}
