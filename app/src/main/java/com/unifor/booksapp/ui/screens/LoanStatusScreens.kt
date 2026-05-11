package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApprovedScreen(onViewLoans: () -> Unit, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Empréstimo Aprovado") }) }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Empréstimo Aprovado!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanUnavailableScreen(onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Livro Indisponível") }) }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Livro Indisponível")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanQueueScreen(queuePosition: Int?, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Fila de Espera") }) }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Você está na posição ${queuePosition ?: "N/A"} da fila.")
        }
    }
}
