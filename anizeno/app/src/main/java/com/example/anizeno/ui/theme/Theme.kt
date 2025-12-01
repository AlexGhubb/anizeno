package com.example.anizeno.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color


//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
//)




// 💜 Colores personalizados
private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)

private val Purple40 = Color(0xFF6650a4)
private val PurpleGrey40 = Color(0xFF625b71)
private val Pink40 = Color(0xFF7D5260)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB5179E),         // Rosa/morado principal
    secondary = Color(0xFF7209B7),       // Morado profundo
    tertiary = Color(0xFF4E2AA1),        // Morado suave
    surface = Color(0xFF2A1B3D),         // Fondo de tarjetas y contenedores
    surfaceVariant = Color(0xFF3D1E60),  // Fondo alternativo de Cards
    background = Color(0xFF1A0B2E),      // Fondo general
    onPrimary = Color.White,
    onSurface = Color(0xFFF5EFFF)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7B2CBF),
    secondary = Color(0xFF9D4EDD),
    tertiary = Color(0xFFC77DFF),
    surface = Color(0xFFF3E8FF),
    surfaceVariant = Color(0xFFEADCFD),
    background = Color(0xFFFDF8FF),
    onPrimary = Color.White,
    onSurface = Color(0xFF1A0B2E)
)

@Composable
fun AnizenoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = colors.onBackground),
            titleLarge = MaterialTheme.typography.titleLarge.copy(color = colors.onPrimary),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(color = colors.onPrimary)
        ),
        content = content
    )
}
