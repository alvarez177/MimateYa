package com.salvarez.mimateyaandroid.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Sample screen to review the theme by eye, in light and dark mode.
 * Temporary: the app shows it until the real screens arrive (step 7).
 */
@Composable
fun DesignSystemShowcase(modifier: Modifier = Modifier) {
    val colors = MimateYaTheme.colors
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = stringResource(R.string.showcase_title), style = typography.displaySmall, color = colors.ink)
        Text(text = stringResource(R.string.showcase_subtitle), style = typography.bodyMedium, color = colors.ink2)

        SectionTitle(stringResource(R.string.showcase_section_text))
        Text(text = stringResource(R.string.showcase_screen_title), style = typography.headlineSmall, color = colors.ink)
        Text(text = stringResource(R.string.showcase_body), style = typography.bodyLarge, color = colors.ink)
        Text(text = stringResource(R.string.showcase_detail), style = typography.bodySmall, color = colors.ink2)
        Text(text = stringResource(R.string.showcase_link), style = typography.labelLarge, color = colors.danger)

        SectionTitle(stringResource(R.string.showcase_section_colors))
        ColorRow(listOf("ink" to colors.ink, "ink2" to colors.ink2, "paper" to colors.paper, "card" to colors.card, "line" to colors.line))
        ColorRow(listOf("accent" to colors.accent, "free" to colors.free, "freeBg" to colors.freeBg, "muted" to colors.muted, "danger" to colors.danger))

        SectionTitle(stringResource(R.string.showcase_section_components))
        PrimaryButtonSample()
        SecondaryButtonSample()
        FreeTag()
        HeroCard()
    }
}

@Composable
private fun SectionTitle(text: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = text.uppercase(), style = MaterialTheme.typography.labelMedium, color = MimateYaTheme.colors.ink2)
}

@Composable
private fun ColorRow(swatches: List<Pair<String, Color>>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for ((name, color) in swatches) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(color, MaterialTheme.shapes.small)
                        .border(1.dp, MimateYaTheme.colors.line, MaterialTheme.shapes.small),
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MimateYaTheme.colors.ink2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun PrimaryButtonSample() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(text = stringResource(R.string.showcase_primary_button), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun SecondaryButtonSample() {
    val accentText = MimateYaTheme.colors.accentText

    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, accentText),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = accentText),
    ) {
        Text(text = stringResource(R.string.showcase_secondary_button), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun FreeTag() {
    Text(
        text = stringResource(R.string.showcase_free_tag),
        style = MaterialTheme.typography.labelMedium,
        color = MimateYaTheme.colors.free,
        modifier = Modifier
            .background(MimateYaTheme.colors.freeBg, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    )
}

@Composable
private fun HeroCard() {
    val colors = MimateYaTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.heroBg, MaterialTheme.shapes.extraLarge)
            .padding(18.dp),
    ) {
        Text(
            text = stringResource(R.string.showcase_hero_caption).uppercase(),
            style = MaterialTheme.typography.bodySmall,
            color = colors.heroSub,
        )
        Row {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .size(24.dp)
                    .background(colors.free, MaterialTheme.shapes.extraLarge),
            )
            Text(text = stringResource(R.string.showcase_hero_title), style = MaterialTheme.typography.displayMedium, color = colors.heroInk)
        }
    }
}

@Preview(name = "Claro", showBackground = true, heightDp = 1100)
@Composable
private fun DesignSystemShowcaseLightPreview() {
    MimateYaTheme(darkTheme = false) {
        DesignSystemShowcase()
    }
}

@Preview(name = "Oscuro", showBackground = true, heightDp = 1100)
@Composable
private fun DesignSystemShowcaseDarkPreview() {
    MimateYaTheme(darkTheme = true) {
        DesignSystemShowcase()
    }
}
