package com.salvarez.mimateyaandroid.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Every color token of the approved mockups (docs/designs/mockups/v2.html), with the
 * same names. Material 3 has no place for some of them (free, muted, hero), so the
 * screens read them from here: MimateYaTheme.colors.free
 */
@Immutable
data class MimateYaColors(
    /** Main text. */
    val ink: Color,
    /** Secondary text. */
    val ink2: Color,
    /** Screen background. */
    val paper: Color,
    /** Cards, inputs and sheets. */
    val card: Color,
    /** Borders and dividers. */
    val line: Color,
    /** Chips and skeleton loaders. */
    val chip: Color,
    /** Main buttons and the floating button. */
    val accent: Color,
    /** Links, secondary buttons and the active tab (lighter than accent in dark mode). */
    val accentText: Color,
    /** Text on top of accent. */
    val accentInk: Color,
    /** Only for what is free by a benefit. */
    val free: Color,
    val freeBg: Color,
    /** Dimmed text, such as inactive tabs. */
    val muted: Color,
    /** Errors and destructive actions such as "Cerrar sesión". */
    val danger: Color,
    /** Highlighted card: next appointment and "Corte gratis". */
    val heroBg: Color,
    val heroInk: Color,
    val heroSub: Color,
    /** Empty part of progress bars (punch card dots). */
    val track: Color,
)

val LightMimateYaColors = MimateYaColors(
    ink = Color(0xFF12161C),
    ink2 = Color(0xFF4A5260),
    paper = Color(0xFFFAFAF8),
    card = Color(0xFFFFFFFF),
    line = Color(0xFFE3E5E8),
    chip = Color(0xFFEEF0F3),
    accent = Color(0xFF1D4ED8),
    accentText = Color(0xFF1D4ED8),
    accentInk = Color(0xFFFFFFFF),
    free = Color(0xFF0E7A4E),
    freeBg = Color(0xFFDDF3E8),
    muted = Color(0xFF8A919C),
    danger = Color(0xFFB42318),
    heroBg = Color(0xFF12161C),
    heroInk = Color(0xFFFFFFFF),
    heroSub = Color(0xFFB8BFCA),
    track = Color(0xFFE3E5E8),
)

val DarkMimateYaColors = MimateYaColors(
    ink = Color(0xFFF2F4F7),
    ink2 = Color(0xFFA9B1BD),
    paper = Color(0xFF0F1318),
    card = Color(0xFF171C23),
    line = Color(0xFF2A313B),
    chip = Color(0xFF232A33),
    accent = Color(0xFF3B63E6),
    accentText = Color(0xFF8FA9FF),
    accentInk = Color(0xFFFFFFFF),
    free = Color(0xFF5BD69A),
    freeBg = Color(0xFF14301F),
    muted = Color(0xFF8A919C),
    danger = Color(0xFFFF8A80),
    heroBg = Color(0xFF1E2530),
    heroInk = Color(0xFFF2F4F7),
    heroSub = Color(0xFFA9B1BD),
    track = Color(0xFF2A313B),
)
