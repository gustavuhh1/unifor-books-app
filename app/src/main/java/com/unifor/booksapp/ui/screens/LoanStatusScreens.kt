package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

// ─────────────────────────────────────────────────────────────
// TELA 1: VOCÊ ESTÁ NA FILA (exibida ao solicitar empréstimo)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanQueueScreen(
    queuePosition: Int? = null,
    bookTitle: String = "",
    bookAuthor: String = "",
    onBack: () -> Unit = {},
    onViewLoans: () -> Unit = {}
) {
    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            StatusIcon(
                icon = Icons.Default.CheckCircle,
                tint = UniforSuccess,
                backgroundColor = UniforSuccessContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Você está na fila",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Infelizmente o livro está em empréstimo. Entraremos em contato quando chegar na sua posição da fila.",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card: Posição na fila
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = UniforSurface,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SUA POSIÇÃO NA FILA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = UniforOutline,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (queuePosition != null) "${queuePosition}°" else "N/A",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black,
                        color = UniforPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoanBookInfoCard(bookTitle = bookTitle, bookAuthor = bookAuthor)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onViewLoans,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text("Ver Meus Empréstimos", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
// TELA 2: EMPRÉSTIMO APROVADO (admin aprovou)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApprovedScreen(
    pickupDeadline: String? = null,
    bookTitle: String = "",
    bookAuthor: String = "",
    onViewLoans: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            StatusIcon(
                icon = Icons.Default.CheckCircle,
                tint = UniforSuccess,
                backgroundColor = UniforSuccessContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Empréstimo\nAprovado!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Sua solicitação foi processada com sucesso pelo sistema da Biblioteca Central.",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card: Data limite para retirada
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = UniforSurface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = UniforOutline,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DATA LIMITE PARA RETIRADA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforOutline,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pickupDeadline ?: "A definir",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = UniforPrimary,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = UniforErrorContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = UniforError,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Atenção: Após esta data, a reserva será cancelada automaticamente e o exemplar retornará ao acervo.",
                                fontSize = 12.sp,
                                color = UniforError,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoanBookInfoCard(bookTitle = bookTitle, bookAuthor = bookAuthor)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onViewLoans,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text("Ver Meus Empréstimos", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
// TELA 3: LIVRO INDISPONÍVEL (admin recusou / em uso)
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanUnavailableScreen(
    bookTitle: String = "",
    bookAuthor: String = "",
    onBack: () -> Unit = {},
    onJoinQueue: () -> Unit = {}
) {
    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            StatusIcon(
                icon = Icons.Default.Cancel,
                tint = UniforError,
                backgroundColor = UniforErrorContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Livro Indisponível",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Infelizmente, o exemplar selecionado não pode ser emprestado no momento.",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card: Info do livro
            LoanBookInfoCard(bookTitle = bookTitle, bookAuthor = bookAuthor)

            Spacer(modifier = Modifier.height(16.dp))

            // Card: Status atual
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = UniforSurface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = UniforOutline,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Status Atual: Em uso",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "O livro está atualmente na posse de outro aluno ou em processo de restauro técnico.",
                        fontSize = 13.sp,
                        color = UniforOutline,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seção: O que você pode fazer?
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "O que você pode fazer?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = UniforSurfaceContainerLow,
                    onClick = onJoinQueue
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Bookmarks,
                            contentDescription = null,
                            tint = UniforPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Entrar na fila de reserva",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = UniforPrimary
                            )
                            Text(
                                text = "Você será notificado quando o livro estiver disponível.",
                                fontSize = 12.sp,
                                color = UniforOutline,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = UniforPrimary)
            ) {
                Text("Voltar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
// COMPONENTES COMPARTILHADOS
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanStatusTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

@Composable
private fun StatusIcon(
    icon: ImageVector,
    tint: Color,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
private fun LoanBookInfoCard(
    bookTitle: String,
    bookAuthor: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = UniforSurface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp, 68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(UniforPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = UniforPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = bookTitle.ifBlank { "Título não disponível" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = UniforPrimary
                )
                if (bookAuthor.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bookAuthor,
                        fontSize = 12.sp,
                        color = UniforOutline
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun LoanQueueScreenPreview() {
    LoanQueueScreen(
        queuePosition = 3,
        bookTitle = "Design for the Real World",
        bookAuthor = "Victor Papanek • 1971"
    )
}

@Preview(showBackground = true)
@Composable
private fun LoanApprovedScreenPreview() {
    LoanApprovedScreen(
        pickupDeadline = "24 de Outubro, 2026",
        bookTitle = "Design for the Real World",
        bookAuthor = "Victor Papanek • 1971"
    )
}

@Preview(showBackground = true)
@Composable
private fun LoanUnavailableScreenPreview() {
    LoanUnavailableScreen(
        bookTitle = "Design for the Real World",
        bookAuthor = "Victor Papanek • 1971"
    )
}
