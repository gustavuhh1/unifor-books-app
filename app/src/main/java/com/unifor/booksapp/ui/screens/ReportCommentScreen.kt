package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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

// ─── TopBar (Compartilhada) ───────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportTopBar(onBack: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
            }
        },
        title = {
            Text("Unifor Books", fontWeight = FontWeight.Black, fontSize = 20.sp, color = UniforPrimary)
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
                Icon(Icons.Default.Person, null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}

// ─── Tela 1: Formulário de Denúncia ───────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCommentScreen(
    onSendReport: () -> Unit,
    onBack: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isReasonExpanded by remember { mutableStateOf(false) }
    val reportReasons = listOf("Discurso de ódio", "Spam ou publicidade", "Conteúdo irrelevante")

    Scaffold(
        topBar = { ReportTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            ReportHeader()
            Spacer(modifier = Modifier.height(32.dp))

            // Formulário
            Text("Motivo da Denúncia", fontWeight = FontWeight.Bold, color = UniforOnSurface)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = isReasonExpanded,
                onExpandedChange = { isReasonExpanded = it }
            ) {
                OutlinedTextField(
                    value = reason,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecione o motivo principal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isReasonExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UniforPrimary,
                        unfocusedBorderColor = UniforOutline.copy(alpha = 0.4f)
                    )
                )
                ExposedDropdownMenu(
                    expanded = isReasonExpanded,
                    onDismissRequest = { isReasonExpanded = false }
                ) {
                    reportReasons.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                reason = selectionOption
                                isReasonExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Descrição Opcional", fontWeight = FontWeight.Bold, color = UniforOnSurface)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Forneça detalhes adicionais que nos ajudem a entender o problema...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UniforPrimary,
                    unfocusedBorderColor = UniforOutline.copy(alpha = 0.4f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSendReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary),
                enabled = reason.isNotBlank()
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar Denúncia", fontWeight = FontWeight.Bold)
            }
            Text(
                "AO ENVIAR, VOCÊ CONFIRMA QUE ESTA DENÚNCIA É VERDADEIRA.",
                fontSize = 10.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            ProcessInfoSection()
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ReportHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(UniforErrorContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Report, contentDescription = null, tint = UniforError, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Denunciar Comentário",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Sua denúncia ajuda a manter o Unifor Books um ambiente seguro e acadêmico. Analisaremos o comentário reportado conforme nossas diretrizes de comunidade.",
            fontSize = 15.sp,
            color = UniforOutline,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun ProcessInfoSection() {
    Surface(
        color = UniforSurfaceContainerLow,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = UniforPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Como funciona o processo?", fontWeight = FontWeight.Bold, color = UniforPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ProcessStep("1", "Nossa equipe de curadoria recebe a notificação instantaneamente.")
            ProcessStep("2", "O conteúdo é revisado manualmente com base nos termos de uso da biblioteca.")
            ProcessStep("3", "Se confirmado, o comentário será removido e o autor poderá ser notificado.")
        }
    }
}

@Composable
private fun ProcessStep(number: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(number, color = UniforOutline, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp))
        Text(text, color = UniforOutline, fontSize = 13.sp, lineHeight = 18.sp)
    }
}


// ─── Tela 2: Confirmação da Denúncia ──────────────────────────────────────────

@Composable
fun ReportConfirmationScreen(
    onBackToBook: () -> Unit,
    onGoToHome: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { ReportTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            ConfirmationHeader()
            Spacer(modifier = Modifier.height(32.dp))
            WhatsNextSection()
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onBackToBook,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text("Voltar para o Livro", fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onGoToHome) {
                Text("Ir para o Início", fontWeight = FontWeight.Bold, color = UniforPrimary)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ConfirmationHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(UniforSuccessContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = UniforSuccess, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Denúncia Recebida",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = UniforPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Agradecemos o seu envio. Nossa equipe de curadoria recebeu sua denúncia e o comentário será revisado em até 24 horas úteis.",
            fontSize = 15.sp,
            color = UniforOutline,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun WhatsNextSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = UniforPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("O que acontece agora?", fontWeight = FontWeight.Bold, color = UniforPrimary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        WhatsNextStep("1", "O comentário será analisado manualmente.")
        WhatsNextStep("2", "Caso viole as diretrizes, ele será removido.")
        WhatsNextStep("3", "Você não será notificado sobre a decisão final por privacidade.")
    }
}

@Composable
private fun WhatsNextStep(number: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(UniforSuccess),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = UniforOutline, fontSize = 14.sp, lineHeight = 20.sp)
    }
}
