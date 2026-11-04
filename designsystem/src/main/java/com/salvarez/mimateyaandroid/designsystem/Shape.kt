package com.salvarez.mimateyaandroid.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Corner radius from the mockups. */
val MimateYaShapes = Shapes(
    // "Gratis" tag
    extraSmall = RoundedCornerShape(8.dp),
    // Inputs, hour chips and search suggestions
    small = RoundedCornerShape(12.dp),
    // Main and secondary buttons
    medium = RoundedCornerShape(14.dp),
    // Cards and the floating button
    large = RoundedCornerShape(16.dp),
    // Highlighted card (next appointment, "Corte gratis")
    extraLarge = RoundedCornerShape(18.dp),
)
