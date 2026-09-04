package com.jula1717.welly.presentation.today

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.components.RangeStatusBadge
import com.jula1717.welly.presentation.components.rangeStatus
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun DailyCalorieCard(
    calories: Int,
    targetMin: Int,
    targetMax: Int,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    NutrientCardSurface(
        modifier = modifier,
        containerColor = scheme.primary,
        border = null,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.today_calories_label),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.inversePrimary,
                )
                NutrientAmount(
                    value = calories,
                    unit = stringResource(R.string.unit_kcal),
                    valueFontSize = 32.sp,
                    valueColor = scheme.onPrimary,
                    unitColor = scheme.inversePrimary,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = stringResource(R.string.kcal_range, targetMin, targetMax),
                    fontSize = 12.sp,
                    color = scheme.inversePrimary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            RangeStatusBadge(status = rangeStatus(calories, targetMin, targetMax))
        }
    }
}

@PreviewLightDark
@Composable
private fun DailyCalorieCardPreview() {
    WellyTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DailyCalorieCard(calories = 1620, targetMin = 1800, targetMax = 2000)
            DailyCalorieCard(calories = 1900, targetMin = 1800, targetMax = 2000)
            DailyCalorieCard(calories = 2150, targetMin = 1800, targetMax = 2000)
        }
    }
}
