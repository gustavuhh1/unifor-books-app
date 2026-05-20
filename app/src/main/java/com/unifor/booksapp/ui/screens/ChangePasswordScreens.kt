package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

// ─────────────────────────────────────────────────────────────
// TELA 1: FORMULÁRIO DE ALTERAR SENHA
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onPasswordChanged: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }

    val hasMinLength = newPassword.length >= 8
    val hasMinNumbers = newPassword.count { it.isDigit() } >= 2
    val hasLowercase = newPassword.any { it.isLowerCase() }
    val isFormValid = currentPassword.isNotBlank() && hasMinLength && hasMinNumbers && hasLowercase

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
                    }
                },
                actions = {
                    Box(
                        Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(UniforPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = UniforPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
            )
        },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Alterar senha",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Insira a sua senha atual e nova senha",
                fontSize = 14.sp,
                color = UniforOutline
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Requisitos da senha
            PasswordRequirement("Senha deve conter no mínimo 8 caracteres", hasMinLength)
            PasswordRequirement("Senha deve conter no mínimo, dois números", hasMinNumbers)
            PasswordRequirement("Senha deve conter no mínimo, uma letra maiúscula", hasLowercase)

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Senha atual
            Text(
                text = "SENHA ATUAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = UniforOutline,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Insira sua senha", color = UniforOutline.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = UniforOutline) },
                trailingIcon = {
                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                        Icon(
                            if (currentPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar senha",
                            tint = UniforOutline
                        )
                    }
                },
                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UniforPrimary,
                    unfocusedBorderColor = UniforSurfaceContainerHigh,
                    unfocusedContainerColor = UniforSurface,
                    focusedContainerColor = UniforSurface
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo: Nova senha
            Text(
                text = "NOVA SENHA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = UniforOutline,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Insira a nova senha", color = UniforOutline.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = UniforOutline) },
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            if (newPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar senha",
                            tint = UniforOutline
                        )
                    }
                },
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UniforPrimary,
                    unfocusedBorderColor = UniforSurfaceContainerHigh,
                    unfocusedContainerColor = UniforSurface,
                    focusedContainerColor = UniforSurface
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onPasswordChanged,
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UniforPrimary,
                    disabledContainerColor = UniforSurfaceContainerHigh
                )
            ) {
                Text(
                    "Alterar senha",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isFormValid) Color.White else UniforOutline
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PasswordRequirement(text: String, isMet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "• ",
            fontSize = 13.sp,
            color = if (isMet) UniforSuccess else UniforOutline
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (isMet) UniforSuccess else UniforOutline,
            fontWeight = if (isMet) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ─────────────────────────────────────────────────────────────
// TELA 2: SENHA ALTERADA COM SUCESSO
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordSuccessScreen(
    onBackToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackToProfile) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary)
                    }
                },
                actions = {
                    Box(
                        Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(UniforPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = UniforPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
            )
        },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ícone de sucesso
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(UniforSuccessContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = UniforSuccess,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Senha alterada\ncom sucesso",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = UniforPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sua senha foi alterada com sucesso, a partir de agora utilize ela para acessar o Unifor Books",
                fontSize = 15.sp,
                color = UniforOutline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onBackToProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UniforPrimary)
            ) {
                Text("Voltar para meu Perfil", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
