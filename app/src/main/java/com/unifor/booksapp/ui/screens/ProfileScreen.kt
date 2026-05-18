package com.unifor.booksapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unifor.booksapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        topBar = { ProfileTopBar(onBack = onBack) },
        containerColor = UniforBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            ProfileHeader()
            Spacer(Modifier.height(32.dp))
            InfoCard("MATRÍCULA", "202310456", Icons.Default.CreditCard, UniforInfoContainer)
            Spacer(Modifier.height(16.dp))
            InfoCard("CARGO", "Aluno", Icons.Default.School, UniforSuccessContainer)
            Spacer(Modifier.height(16.dp))
            InfoCard("E-MAIL INSTITUCIONAL", "joao.silva@aluno.unifor.br", Icons.Default.Email, UniforWarningContainer)
            Spacer(Modifier.height(16.dp))
            InfoCard("MEMBRO DESDE", "Agosto 2023", Icons.Default.DateRange, UniforSurfaceContainerHigh)
            Spacer(Modifier.height(24.dp))
            ActionButton(text = "Alterar Senha", icon = Icons.Default.Lock, onClick = {})
            Spacer(Modifier.height(16.dp))
            ActionButton(text = "Sair da Conta", icon = Icons.AutoMirrored.Filled.ExitToApp, isDestructive = true, onClick = onLogout)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(UniforPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Avatar", modifier = Modifier.size(80.dp), tint = UniforPrimary)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(UniforPrimary)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Alterar foto", tint = Color.White)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("João Silva", fontSize = 24.sp, fontWeight = FontWeight.Black, color = UniforPrimary)
        Text("Aluno da Unifor", fontSize = 16.sp, color = UniforOutline)
    }
}

@Composable
private fun InfoCard(label: String, value: String, icon: ImageVector, iconBackgroundColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = UniforSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = UniforPrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(label, fontSize = 12.sp, color = UniforOutline, fontWeight = FontWeight.SemiBold)
                Text(value, fontSize = 16.sp, color = UniforPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ActionButton(text: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false) {
    val backgroundColor = if (isDestructive) UniforErrorContainer else UniforSurface
    val contentColor = if (isDestructive) UniforError else UniforPrimary

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = text, tint = contentColor)
            Spacer(Modifier.width(8.dp))
            Text(text, color = contentColor, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Unifor Books", fontWeight = FontWeight.Black, color = UniforPrimary) },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = UniforPrimary) } },
        actions = {
             Box(Modifier.padding(end = 16.dp).size(40.dp).clip(CircleShape).background(UniforPrimary.copy(0.1f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = UniforPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = UniforBackground)
    )
}
