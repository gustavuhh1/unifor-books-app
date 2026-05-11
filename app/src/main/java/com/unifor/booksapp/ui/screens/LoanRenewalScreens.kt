package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RenewalTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary) } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

@Composable
fun RenewalAvailableScreen(onBack: () -> Unit, onGoToCollection: () -> Unit, newDueDate: String?) {
    Scaffold(topBar = { RenewalTopBar(onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.CheckCircle, null, tint = UniforSuccess, modifier = Modifier.size(72.dp))
            Spacer(Modifier.height(24.dp))
            Text("Renovação Disponível", fontSize = 28.sp, fontWeight = FontWeight.Black, color = UniforPrimary, textAlign = TextAlign.Center)
            Text("Não há ninguém na fila de espera para este livro. Sua nova data de devolução será em 2 semanas.", textAlign = TextAlign.Center, color = UniforOutline, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(24.dp))
            if (newDueDate != null) {
                Surface(shape = RoundedCornerShape(16.dp), color = UniforSurface, shadowElevation = 1.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Text("NOVA DATA DE DEVOLUÇÃO", fontSize = 11.sp, color = UniforOutline)
                        Text(newDueDate, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = UniforPrimary)
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Row {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Voltar") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onGoToCollection, modifier = Modifier.weight(1f)) { Text("Ver Acervo Digital") }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun RenewalUnavailableScreen(onBack: () -> Unit, onGoToCollection: () -> Unit) {
    Scaffold(topBar = { RenewalTopBar(onBack = onBack) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Block, null, tint = UniforError, modifier = Modifier.size(72.dp))
            Spacer(Modifier.height(24.dp))
            Text("Renovação Indisponível", fontSize = 28.sp, fontWeight = FontWeight.Black, color = UniforPrimary, textAlign = TextAlign.Center)
            Text("Desculpe, existem outros alunos na fila de espera para este exemplar. A renovação não é permitida neste momento.", textAlign = TextAlign.Center, color = UniforOutline, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.weight(1f))
             Row {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Voltar") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onGoToCollection, modifier = Modifier.weight(1f)) { Text("Ver Acervo Digital") }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
