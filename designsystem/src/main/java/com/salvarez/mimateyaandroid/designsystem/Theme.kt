package com.salvarez.mimateyaandroid.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalMimateYaColors = staticCompositionLocalOf { LightMimateYaColors }

/**
 * The app theme. Light or dark follows the system setting. Dynamic color (Android 12+)
 * is off on purpose: the approved mockup colors are always used.
 */
@Composable
fun MimateYaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkMimateYaColors else LightMimateYaColors
    val colorScheme = materialColorScheme(colors, darkTheme)

    CompositionLocalProvider(LocalMimateYaColors provides colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MimateYaTypography,
            shapes = MimateYaShapes,
            content = content,
        )
    }
}

/** Shortcut to the tokens that Material 3 does not have: MimateYaTheme.colors.free */
object MimateYaTheme {
    val colors: MimateYaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMimateYaColors.current
}

/** Maps the mockup tokens to Material 3, so Material components get the right colors. */
private fun materialColorScheme(colors: MimateYaColors, darkTheme: Boolean): ColorScheme {
    val base = if (darkTheme) darkColorScheme() else lightColorScheme()

    return base.copy(
        primary = colors.accent,
        onPrimary = colors.accentInk,
        secondary = colors.free,
        onSecondary = colors.paper,
        secondaryContainer = colors.freeBg,
        onSecondaryContainer = colors.free,
        background = colors.paper,
        onBackground = colors.ink,
        surface = colors.card,
        onSurface = colors.ink,
        surfaceVariant = colors.chip,
        onSurfaceVariant = colors.ink2,
        surfaceContainer = colors.card,
        outline = colors.line,
        outlineVariant = colors.line,
        error = colors.danger,
        onError = colors.accentInk,
        inverseSurface = colors.heroBg,
        inverseOnSurface = colors.heroInk,
    )
}
