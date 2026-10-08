package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = UtkarshRed,
    onPrimary = Color.White,
    primaryContainer = UtkarshRedDark,
    onPrimaryContainer = Color.White,
    secondary = UtkarshPink,
    onSecondary = Color.White,
    background = AppleBlack,
    onBackground = AppleDarkTextPrimary,
    surface = AppleDarkSurface,
    onSurface = AppleDarkTextPrimary,
    surfaceVariant = AppleDarkCard,
    onSurfaceVariant = AppleDarkTextSecondary,
    outline = AppleDarkBorder,
    outlineVariant = AppleDarkTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = UtkarshRed,
    onPrimary = Color.White,
    primaryContainer = UtkarshPink,
    onPrimaryContainer = Color.White,
    secondary = UtkarshRedDark,
    onSecondary = Color.White,
    background = AppleLightBg,
    onBackground = AppleLightTextPrimary,
    surface = AppleLightCard,
    onSurface = AppleLightTextPrimary,
    surfaceVariant = AppleLightSecondaryCard,
    onSurfaceVariant = AppleLightTextSecondary,
    outline = AppleLightBorder,
    outlineVariant = AppleLightSecondaryCard
)

@Composable
fun UtkarshMusicTheme(
    darkTheme: Boolean = true, // Default to sleek Apple Music dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
