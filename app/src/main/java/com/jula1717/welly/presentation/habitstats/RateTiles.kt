package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitRate
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun RateTiles(
    last7Days: HabitRate?,
    last30Days: HabitRate?,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RateTile(
            title = stringResource(R.string.habit_stats_last_7_days),
            rate = last7Days,
            modifier = Modifier.weight(1f),
        )
        RateTile(
            title = stringResource(R.string.habit_stats_last_30_days),
            rate = last30Days,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RateTile(
    title: String,
    rate: HabitRate?,
    modifier: Modifier = Modifier,
) {
    StatsCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProgressRing(
                fraction = rate?.let { it.percent / PERCENT_FULL } ?: 0f,
                label = rate?.let { stringResource(R.string.habit_stats_rate_percent, it.percent) }
                    ?: stringResource(R.string.habit_stats_no_rate),
            )
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = rate?.let { stringResource(R.string.habit_stats_rate_of, it.met, it.total) }
                    ?: stringResource(R.string.habit_stats_no_data),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ProgressRing(
    fraction: Float,
    label: String,
    modifier: Modifier = Modifier,
) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainer
    val progressColor = MaterialTheme.colorScheme.primaryContainer

    Box(modifier = modifier.size(RING_SIZE), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(RING_SIZE)) {
            val stroke = Stroke(width = RING_STROKE.toPx(), cap = StrokeCap.Round)
            val inset = stroke.width / 2
            val arcSize = Size(size.width - stroke.width, size.height - stroke.width)
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = FULL_CIRCLE,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
            if (fraction > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = START_ANGLE,
                    sweepAngle = FULL_CIRCLE * fraction.coerceAtMost(1f),
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = stroke,
                )
            }
        }
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val RING_SIZE = 70.dp
private val RING_STROKE = 7.dp
private const val FULL_CIRCLE = 360f
private const val START_ANGLE = -90f
private const val PERCENT_FULL = 100f

@PreviewLightDark
@Composable
private fun RateTilesPreview() {
    WellyTheme {
        RateTiles(
            last7Days = HabitRate(met = 2, total = 2),
            last30Days = HabitRate(met = 7, total = 8),
            modifier = Modifier.padding(16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun RateTilesNoDataPreview() {
    WellyTheme {
        RateTiles(
            last7Days = null,
            last30Days = null,
            modifier = Modifier.padding(16.dp),
        )
    }
}
