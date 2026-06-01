package com.unifor.booksapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Mantemos as definições, mas por enquanto o App focará no Light Mode 
// para garantir 100% de fidelidade ao protótipo React fornecido.
private val DarkColorScheme = darkColorScheme(
    primary = UniforPrimary,
    secondary = UniforOutline,
    tertiary = UniforTertiary,
    background = UniforBackground, // Usando fundo claro mesmo no Dark para evitar tela preta
    surface = UniforSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = UniforOnSurface,
    onSurface = UniforOnSurface,
    outline = UniforOutline
)

private val LightColorScheme = lightColorScheme(
    primary = UniforPrimary,
    secondary = UniforOutline,
    tertiary = UniforTertiary,
    background = UniforBackground, // #F8F9FA
    surface = UniforSurface,       // #FFFFFF
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = UniforOnSurface,
    onSurface = UniforOnSurface,
    outline = UniforOutline
)

@Composable
fun UniforBooksAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // IMPORTANTE: Forçamos LightColorScheme para alinhar com o design React.
    // Se quiser habilitar Dark Mode no futuro, basta voltar para:
    // val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}