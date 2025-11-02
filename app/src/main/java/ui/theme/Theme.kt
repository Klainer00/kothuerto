package ui.theme
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores claros
private val LightColorScheme = lightColorScheme(
    primary = HuertoGreenDark,
    secondary = HuertoGreen,
    tertiary = HuertoBrown,
    background = HuertoBeige,
    surface = HuertoWhite,
    onPrimary = HuertoWhite,
    onSecondary = HuertoWhite,
    onTertiary = HuertoWhite,
    onBackground = HuertoBlack,
    onSurface = HuertoBlack,
    error = HuertoRed,
    onError = HuertoWhite
)

// Paleta de colores oscuros (opcional, por ahora similar al claro)
private val DarkColorScheme = darkColorScheme(
    primary = HuertoGreen,
    secondary = HuertoBrown,
    tertiary = HuertoBeige,
    background = HuertoGreenDark,
    surface = Color(0xFF1C1B1F),
    onPrimary = HuertoWhite,
    onSecondary = HuertoBlack,
    onTertiary = HuertoBlack,
    onBackground = HuertoWhite,
    onSurface = HuertoWhite,
    error = HuertoRed,
    onError = HuertoWhite
)

@Composable
fun Huerto1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
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
        typography = Typography, // Asume que Typography.kt existe (puedes usar el default)
        content = content
    )
}
