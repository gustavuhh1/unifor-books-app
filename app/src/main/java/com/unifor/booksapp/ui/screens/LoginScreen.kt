package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unifor.booksapp.ui.theme.*
import com.unifor.booksapp.ui.viewmodels.LoginUiState
import com.unifor.booksapp.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    val matricula by loginViewModel.matricula.collectAsState()
    val senha by loginViewModel.senha.collectAsState()
    var rememberMe by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        LogoComponent()
        Spacer(modifier = Modifier.height(32.dp))
        HeaderComponent()
        Spacer(modifier = Modifier.height(32.dp))

        InputComponent(
            label = "MATRÍCULA",
            value = matricula,
            onValueChange = { loginViewModel.matricula.value = it },
            placeholder = "Digite sua matrícula",
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputComponent(
            label = "SENHA",
            value = senha,
            onValueChange = { loginViewModel.senha.value = it },
            placeholder = "Sua senha de acesso",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            trailingAction = {
                Text(
                    text = "Esqueceu a senha?",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        RememberMeComponent(checked = rememberMe, onCheckedChange = { rememberMe = it })

        // Exibe erro se houver
        if (uiState is LoginUiState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = UniforErrorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = (uiState as LoginUiState.Error).message,
                    color = UniforError,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LoginButton(
            onClick = { loginViewModel.login() },
            isLoading = uiState is LoginUiState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))
        SupportSection()
        Spacer(modifier = Modifier.height(40.dp))
        CopyrightSection()
    }
}

@Composable
fun LogoComponent() {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary
    val iconColor = if (isDark) UniforBlue else Color.White
    val textColor = if (isDark) UniforBlue else Color.White

    Box(
        modifier = Modifier
            .size(width = 120.dp, height = 120.dp)
            .background(bgColor, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(48.dp)
            )
            Text(text = "Unifor", color = textColor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

@Composable
fun HeaderComponent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Bem-vindo à Biblioteca",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Acesse o acervo digital e serviços acadêmicos",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun InputComponent(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    trailingAction: @Composable (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            trailingAction?.invoke()
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.secondary) },
            leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun RememberMeComponent(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
        )
        Text(text = "Mantenha-me conectado", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
    }
}

@Composable
fun LoginButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = UniforDarkBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Entrar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun SupportSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SupportButton(icon = Icons.Default.Help, text = "SUPORTE", modifier = Modifier.weight(1f))
        SupportButton(icon = Icons.Default.Email, text = "CONTATO", modifier = Modifier.weight(1f))
    }
}

@Composable
fun SupportButton(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    TextButton(onClick = { }, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = if (isDark) UniforTextGray else UniforBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = UniforTextGray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CopyrightSection() {
    Text(
        text = "© 2024 FUNDAÇÃO EDSON QUEIROZ • UNIVERSIDADE DE FORTALEZA",
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 10.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreviewLight() {
    UniforBooksAppTheme(darkTheme = false) {
        LoginScreen(onLoginSuccess = {})
    }
}
