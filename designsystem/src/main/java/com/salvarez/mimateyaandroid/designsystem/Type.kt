package com.salvarez.mimateyaandroid.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/*
 * Fonts are bundled in res/font (no download from Google at runtime). Each one is a
 * variable font: a single file holds every weight, and FontVariation picks the weight.
 * Font(..., variationSettings) is still marked experimental in Compose, hence the opt-in.
 */

@OptIn(ExperimentalTextApi::class)
private fun bricolageGrotesque(weight: FontWeight): Font {
    val variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))

    return Font(R.font.bricolage_grotesque, weight = weight, variationSettings = variationSettings)
}

@OptIn(ExperimentalTextApi::class)
private fun figtree(weight: FontWeight): Font {
    val variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))

    return Font(R.font.figtree, weight = weight, variationSettings = variationSettings)
}

/** Titles, times and buttons. */
val DisplayFontFamily = FontFamily(
    bricolageGrotesque(FontWeight.Medium),
    bricolageGrotesque(FontWeight.Bold),
    bricolageGrotesque(FontWeight.ExtraBold),
)

/** Everything else. */
val BodyFontFamily = FontFamily(
    figtree(FontWeight.Normal),
    figtree(FontWeight.Medium),
    figtree(FontWeight.SemiBold),
)

/** Sizes taken from the mockups. The comment says where each style is used. */
val MimateYaTypography = Typography(
    // Big number or message: "Corte gratis"
    displayMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
    ),
    // Sign up title: "Crea tu cuenta"
    displaySmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
    ),
    // Screen titles: "Nuevo plan", "Perfil"
    headlineMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
    ),
    // Agenda date and "Nuevo turno"
    headlineSmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.22).sp,
    ),
    // Confirmation message after completing a cut
    titleLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    // Main buttons
    titleMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    // Appointment time, hour chips and avatar initials
    titleSmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    // Normal text: client names, inputs
    bodyLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // Subtitles and hints
    bodyMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Small details: "10 cortes por $150.000"
    bodySmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Links: "Volver", "Revertir"
    labelLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    // Field labels, section titles and the "Gratis" tag
    labelMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Chips and bottom bar
    labelSmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
