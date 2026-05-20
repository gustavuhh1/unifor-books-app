package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinesPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guia de Multas", fontWeight = FontWeight.Bold, color = UniforPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
            )
        },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Guia de Política de Multas", fontSize = 28.sp, fontWeight = FontWeight.Black, color = UniforPrimary)
            Text(
                "Garantir o acesso equitativo à nossa coleção começa com devoluções pontuais. Entenda como funciona nosso sistema de empréstimos e mecanismos de multa.",
                color = UniforOutline,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(Modifier.height(32.dp))
            PolicyCard(
                icon = { Icon(Icons.Default.Bookmark, null, tint = UniforPrimary) },
                title = "Regras para Devoluções",
                subtitle = "Período de 2 semanas",
                body = "Duração padrão de empréstimo para todos os itens de circulação geral. Renovações disponíveis se não houver reservas."
            )
            Spacer(Modifier.height(16.dp))
            PolicyCard(
                icon = { Icon(Icons.Default.Warning, null, tint = UniforError) },
                title = "Penalidades por Atraso",
                subtitle = null,
                body = "As multas começam a acumular diariamente após o prazo. Sua conta será temporariamente suspensa para novos empréstimos até a regularização.",
                containerColor = UniforErrorContainer
            )
        }
    }
}

@Composable
private fun PolicyCard(icon: @Composable () -> Unit, title: String, subtitle: String?, body: String, containerColor: Color = UniforSurface) {
    Surface(shape = RoundedCornerShape(16.dp), color = containerColor, shadowElevation = if (containerColor == UniforSurface) 1.dp else 0.dp) {
        Column(Modifier.padding(20.dp)) {
            Row {
                icon()
                Spacer(Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = UniforPrimary)
            }
            if (subtitle != null) {
                Text(subtitle, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = UniforPrimary, modifier = Modifier.padding(top = 12.dp))
            }
            Text(body, color = UniforOutline, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
