package com.jula1717.welly.presentation.today

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.components.MacroType
import com.jula1717.welly.presentation.components.RangeStatusBadge
import com.jula1717.welly.presentation.components.displayNameResId
import com.jula1717.welly.presentation.components.rangeStatus
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun MacroTargetCard(
    type: MacroType,
    grams: Int,
    targetMin: Int,
    targetMax: Int,
    modifier: Modifier = Modifier,
) {
    NutrientCardSurface(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column {
            Text(
                text = stringResource(type.displayNameResId),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NutrientAmount(
                    value = grams,
                    unit = stringResource(R.string.unit_g),
                    valueFontSize = 20.sp,
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    unitColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                RangeStatusBadge(status = rangeStatus(grams, targetMin, targetMax))
            }
            Text(
                text = stringResource(R.string.gram_range, targetMin, targetMax),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MacroTargetCardGridPreview() {
    WellyTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MacroTargetCard(
                    type = MacroType.PROTEIN,
                    grams = 82,
                    targetMin = 100,
                    targetMax = 120,
                    modifier = Modifier.weight(1f),
                )
                MacroTargetCard(
                    type = MacroType.CARBS,
                    grams = 210,
                    targetMin = 200,
                    targetMax = 235,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MacroTargetCard(
                    type = MacroType.FAT,
                    grams = 88,
                    targetMin = 55,
                    targetMax = 70,
                    modifier = Modifier.weight(1f),
                )
                MacroTargetCard(
                    type = MacroType.FIBER,
                    grams = 30,
                    targetMin = 25,
                    targetMax = 35,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MacroTargetCardPreview() {
    WellyTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MacroTargetCard(type = MacroType.PROTEIN, grams = 82, targetMin = 100, targetMax = 120)
            MacroTargetCard(type = MacroType.CARBS, grams = 210, targetMin = 200, targetMax = 235)
            MacroTargetCard(type = MacroType.FAT, grams = 88, targetMin = 55, targetMax = 70)
        }
    }
}
