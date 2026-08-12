package com.jula1717.welly.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ParsedMacrosBar(
    macros: MealMacros,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(9.dp),
            )
        }
        MacroChipItem(type = MacroType.CALORIES, value = macros.calories)
        MacroChipItem(type = MacroType.PROTEIN, value = macros.protein)
        MacroChipItem(type = MacroType.CARBS, value = macros.carbs)
        MacroChipItem(type = MacroType.FAT, value = macros.fat)
        MacroChipItem(type = MacroType.FIBER, value = macros.fiber)
    }
}

@PreviewLightDark
@Composable
private fun ParsedMacrosBarPreview() {
    WellyTheme {
        ParsedMacrosBar(
            macros = MealMacros(
                calories = 455,
                protein = 30,
                carbs = 50,
                fat = 15,
                fiber = 5,
            ),
        )
    }
}
