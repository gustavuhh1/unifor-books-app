package com.unifor.booksapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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

            Spacer(Modifier.height(40.dp))

            // ── Perguntas Frequentes ──────────────────────────────────
            Text(
                text = "Perguntas Frequentes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary
            )
            Spacer(Modifier.height(16.dp))

            val faqs = listOf(
                FaqItem(
                    question = "Como faço para pagar minhas multas?",
                    answer = "Os pagamentos podem ser feitos através do aplicativo móvel Unifor, Portal do Aluno (NetU) ou no balcão de circulação da Biblioteca usando cartões de crédito/débito."
                ),
                FaqItem(
                    question = "Posso renovar um item que já está em atraso?",
                    answer = "Não. Quando um item está em atraso, as renovações são bloqueadas. Você deve devolver o item e quitar a multa antes de emprestar ou renovar novamente."
                ),
                FaqItem(
                    question = "O que acontece se eu perder um livro?",
                    answer = "Em caso de perda, o aluno é responsável pelo custo de reposição da edição mais recente disponível, acrescido de uma taxa administrativa de processamento."
                )
            )

            faqs.forEach { faq ->
                FaqCard(faq = faq)
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private data class FaqItem(val question: String, val answer: String)

@Composable
private fun FaqCard(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_rotation"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = UniforSurface,
        shadowElevation = 1.dp,
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = UniforPrimary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Recolher" else "Expandir",
                    tint = UniforPrimary,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(arrowRotation)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    color = UniforOutline,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun PolicyCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    body: String,
    containerColor: Color = UniforSurface
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        shadowElevation = if (containerColor == UniforSurface) 1.dp else 0.dp
    ) {
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
