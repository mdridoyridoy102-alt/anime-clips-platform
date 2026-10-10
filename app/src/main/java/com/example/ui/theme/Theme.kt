package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnimeClipsColorScheme = darkColorScheme(
    primary = BrandOrange,
    onPrimary = TextDark,
    primaryContainer = BrandOrangeDark,
    onPrimaryContainer = TextPrimary,
    secondary = AccentCyan,
    onSecondary = TextDark,
    secondaryContainer = SurfaceHighlight,
    onSecondaryContainer = AccentCyan,
    tertiary = AccentGreen,
    onTertiary = TextDark,
    background = BackgroundNearBlack,
    onBackground = TextPrimary,
    surface = SurfaceCharcoal,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderMedium
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnimeClipsColorScheme,
        typography = Typography,
        content = content
    )
}
