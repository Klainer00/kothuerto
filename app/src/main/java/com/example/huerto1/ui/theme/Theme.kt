package com.example.huerto1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
// --- ESTA ES LA IMPORTACIÓN QUE FALTA ---
import androidx.compose.ui.graphics.Color

// Verdes (Principal) - Tema "Huerto"
val Green40 = Color(0xFF4CAF50) // Verde principal (para tema claro)
val Green10 = Color(0xFF003300) // Verde oscuro (para texto sobre contenedor claro)
val Green80 = Color(0xFF9CCC65) // Verde principal (para tema oscuro)
val Green20 = Color(0xFF1B5E20) // Verde oscuro (para texto sobre contenedor oscuro)
val Green90 = Color(0xFFE8F5E9) // Contenedor (claro)
val Green30 = Color(0xFF388E3C) // Contenedor (oscuro)

// Marrones (Secundario) - Tema "Tierra"
val Brown40 = Color(0xFF795548) // Marrón principal (claro)
val Brown10 = Color(0xFF3E2723) // Marrón oscuro (texto)
val Brown80 = Color(0xFFA1887F) // Marrón principal (oscuro)
val Brown20 = Color(0xFF4E342E) // Marrón oscuro (texto)
val Brown90 = Color(0xFFEFEBE9) // Contenedor (claro)
val Brown30 = Color(0xFF5D4037) // Contenedor (oscuro)

// Beiges (Terciario) - Tema "Sol/Trigo"
val Beige40 = Color(0xFFFFEB3B) // Un beige/amarillo pálido (claro)
val Beige10 = Color(0xFF33301F) // Texto para beige
val Beige80 = Color(0xFFFFF59D) // Beige (oscuro)
val Beige20 = Color(0xFF5C5B4E) // Texto para beige
val Beige90 = Color(0xFFFFFDE7) // Contenedor (claro)
val Beige30 = Color(0xFFF0E68C) // Contenedor (oscuro)

// Rojos (Error)
val Red40 = Color(0xFFF44336)
val Red10 = Color(0xFF4D0000)
val Red80 = Color(0xFFE57373)
val Red20 = Color(0xFFB71C1C)
val Red90 = Color(0xFFFFEBEE)
val Red30 = Color(0xFFD32F2F)

// Grises (Fondo/Superficie)
val Grey10 = Color(0xFF1C1C1C) // Fondo oscuro
val Grey90 = Color(0xFFE0E0E0) // Texto sobre fondo oscuro
val Grey99 = Color(0xFFFBFBFB) // Fondo claro

// Variantes de Verde/Gris (Superficies Neutrales)
val GreenGrey10 = Color(0xFF1B1D1B)
val GreenGrey90 = Color(0xFFE3E3E3)
val GreenGrey30 = Color(0xFF434943)
val GreenGrey80 = Color(0xFFC3C9C1)
val GreenGrey60 = Color(0xFF8F958D)
val GreenGrey99 = Color(0xFFF8FAF6)
val GreenGrey50 = Color(0xFF757B73)
// Paleta de colores oscuros para el huerto
private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = Brown80,
    onSecondary = Brown20,
    secondaryContainer = Brown30,
    onSecondaryContainer = Brown90,
    tertiary = Beige80,
    onTertiary = Beige20,
    tertiaryContainer = Beige30,
    onTertiaryContainer = Beige90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Grey10,
    onBackground = Grey90,
    surface = GreenGrey10,
    onSurface = GreenGrey90,
    surfaceVariant = GreenGrey30,
    onSurfaceVariant = GreenGrey80,
    outline = GreenGrey60
)

// Paleta de colores claros para el huerto
private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = Brown40,
    onSecondary = Color.White,
    secondaryContainer = Brown90,
    onSecondaryContainer = Brown10,
    tertiary = Beige40,
    onTertiary = Color.White,
    tertiaryContainer = Beige90,
    onTertiaryContainer = Beige10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = GreenGrey99,
    onSurface = GreenGrey10,
    surfaceVariant = GreenGrey90,
    onSurfaceVariant = GreenGrey30,
    outline = GreenGrey50
)

@Composable
fun Huerto1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

