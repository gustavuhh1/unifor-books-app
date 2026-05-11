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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

// ─── Shared components ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanStatusTopBar(onBack: () -> Unit) {
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

/** Mini card do livro exibido no rodapé das telas de status.
 *  Título e autor virão do banco — vazios por enquanto. */
@Composable
private fun BookMiniCard(
    title: String = "",
    author: String = ""
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = UniforSurfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Capa placeholder
            Box(
                modifier = Modifier
                    .size(width = 52.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(UniforSurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = UniforOutline.copy(alpha = 0.4f),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = UniforOnSurface,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(author, fontSize = 12.sp, color = UniforOutline)
            }
        }
    }
}

/** Ícone circular com fundo colorido usado como status visual principal */
@Composable
private fun StatusIcon(
    icon: ImageVector,
    iconTint: Color,
    containerColor: Color,
    size: Int = 72
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size((size * 0.5f).dp)
        )
    }
}

// ─── 1. Empréstimo Aprovado ───────────────────────────────────────────────────

@Composable
fun LoanApprovedScreen(
    onViewLoans: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Ícone de sucesso
            StatusIcon(
                icon = Icons.Default.CheckCircle,
                iconTint = UniforSuccess,
                containerColor = UniforSuccessContainer
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "Empréstimo\nAprovado!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Sua solicitação foi processada com sucesso pelo sistema da Biblioteca Central.",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card data limite
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = UniforSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = UniforOutline,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "DATA LIMITE PARA RETIRADA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforOutline,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Data virá do banco
                    Text(
                        "",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = UniforPrimary,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Aviso de atenção
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = UniforErrorContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = UniforError,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Atenção: Após esta data, a reserva será cancelada automaticamente e o exemplar retornará ao acervo.",
                                fontSize = 12.sp,
                                color = UniforError,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mini card do livro
            BookMiniCard()

            Spacer(modifier = Modifier.height(32.dp))

            // Botão CTA
            Button(
                onClick = onViewLoans,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text(
                    "Ver Meus Empréstimos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ─── 2. Livro Indisponível ────────────────────────────────────────────────────

@Composable
fun LoanUnavailableScreen(
    onJoinQueue: () -> Unit = {},
    onBack: () -> Unit = {},
    onViewDigitalCollection: () -> Unit = {}
) {
    var joinQueueChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Ícone de erro
            StatusIcon(
                icon = Icons.Default.Block,
                iconTint = UniforError,
                containerColor = UniforErrorContainer
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "Livro Indisponível",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = UniforOnSurface,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Infelizmente, o exemplar selecionado não pode ser emprestado no momento.",
                fontSize = 15.sp,
                color = UniforOutline,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card status atual
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = UniforSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(UniforInfoContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = UniforPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        // Status e descrição virão do banco
                        Text(
                            "Status Atual",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = UniforOnSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "",
                            fontSize = 13.sp,
                            color = UniforOutline,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "O que você pode fazer?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = UniforOnSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Opção: entrar na fila
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = UniforSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(UniforWarningContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            tint = UniforWarningOnContainer,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        "Entrar na fila de reserva",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = UniforOnSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = joinQueueChecked,
                        onCheckedChange = { joinQueueChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = UniforPrimary,
                            uncheckedColor = UniforOutline
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botões de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UniforPrimary),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp, UniforSurfaceContainerHigh
                    )
                ) {
                    Text("Voltar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Button(
                    onClick = onViewDigitalCollection,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
                ) {
                    Text(
                        "Ver Acervo Digital",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ─── 3. Você está na Fila ────────────────────────────────────────────────────

@Composable
fun LoanQueueScreen(
    queuePosition: Int? = null,
    onViewLoans: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = { LoanStatusTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Ícone de sucesso (fila confirmada)
            StatusIcon(
                icon = Icons.Default.CheckCircle,
                iconTint = UniforSuccess,
                containerColor = UniforSuccessContainer
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                "Você está na fila",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Infelizmente o livro está em empréstimo, entraremos em contato quando chegar na sua posição da fila.",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card posição na fila
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = UniforSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar posição
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(UniforSurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            tint = UniforPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            "SUA POSIÇÃO NA FILA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = UniforOutline,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Posição virá do banco
                        Text(
                            queuePosition?.let { "$it°" } ?: "",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = UniforPrimary,
                            lineHeight = 40.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mini card do livro
            BookMiniCard()

            Spacer(modifier = Modifier.height(32.dp))

            // Botão CTA
            Button(
                onClick = onViewLoans,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text(
                    "Ver Meus Empréstimos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
