package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitMonthCount
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.format.TextStyle

@Composable
internal fun MonthlyCountChart(
    counts: List<HabitMonthCount>,
    modifier: Modifier = Modifier,
) {
    val locale = LocalLocale.current.platformLocale
    val maxCount = remember(counts) { counts.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1 }

    StatsCard(modifier = modifier) {
        Text(
            text = stringResource(R.string.habit_stats_monthly_title).uppercase(locale),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CHART_HEIGHT),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            counts.forEach { item ->
                MonthBar(
                    count = item.count,
                    barHeight = BAR_MAX_HEIGHT * item.count / maxCount,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            counts.forEach { item ->
                Text(
                    text = item.month.month.getDisplayName(TextStyle.SHORT, locale),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun MonthBar(
    count: Int,
    barHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.Bottom),
    ) {
        Text(
            text = count.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 20.dp)
                .height(barHeight.coerceAtLeast(MIN_BAR_HEIGHT))
                .background(MaterialTheme.colorScheme.inversePrimary, BAR_SHAPE),
        )
    }
}

private val BAR_SHAPE = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp, bottomStart = 2.dp, bottomEnd = 2.dp)
private val CHART_HEIGHT = 70.dp
private val BAR_MAX_HEIGHT = 52.dp
private val MIN_BAR_HEIGHT = 3.dp

@PreviewLightDark
@Composable
private fun MonthlyCountChartPreview() {
    WellyTheme {
        MonthlyCountChart(
            counts = PreviewStats.monthlyCounts,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun MonthlyCountChartEmptyPreview() {
    WellyTheme {
        MonthlyCountChart(
            counts = PreviewStats.monthlyCounts.map { it.copy(count = 0) },
            modifier = Modifier.padding(16.dp),
        )
    }
}
