package com.jula1717.welly.presentation.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun NutrientAmount(
    value: Int,
    unit: String,
    valueFontSize: TextUnit,
    valueColor: Color,
    unitColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "$value",
            fontSize = valueFontSize,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor,
        )
        Text(text = unit, fontSize = 13.sp, color = unitColor)
    }
}

@PreviewLightDark
@Composable
private fun NutrientAmountPreview() {
    WellyTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NutrientAmount(
                value = 1620,
                unit = "kcal",
                valueFontSize = 32.sp,
                valueColor = MaterialTheme.colorScheme.onSurface,
                unitColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            NutrientAmount(
                value = 82,
                unit = "g",
                valueFontSize = 20.sp,
                valueColor = MaterialTheme.colorScheme.onSurface,
                unitColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
