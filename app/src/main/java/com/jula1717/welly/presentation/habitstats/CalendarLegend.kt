package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun CalendarLegend(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LegendItem(color = colors.primaryContainer, label = stringResource(R.string.habit_stats_legend_done))
        LegendItem(color = colors.secondaryContainer, label = stringResource(R.string.habit_stats_legend_covered))
        LegendItem(color = Color.Transparent, label = stringResource(R.string.habit_stats_legend_none), outlined = true)
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    outlined: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        val outline = if (outlined) MaterialTheme.colorScheme.outlineVariant else Color.Transparent
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
                .border(1.dp, outline, CircleShape),
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@PreviewLightDark
@Composable
private fun CalendarLegendPreview() {
    WellyTheme {
        CalendarLegend(modifier = Modifier.padding(16.dp))
    }
}
