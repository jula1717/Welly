package com.jula1717.welly.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.domain.model.HabitDaySummary
import com.jula1717.welly.presentation.util.displayName
import com.jula1717.welly.presentation.util.periodProgressText
import com.jula1717.welly.presentation.util.streakText
import com.jula1717.welly.ui.theme.WellyTheme


@Composable
internal fun HabitItem(
    summary: HabitDaySummary,
    onClick: () -> Unit,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val containerColor =
        if (summary.habit.isBuiltIn) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLowest
        }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(shape)
            .background(containerColor)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        HabitSummaryText(summary = summary, modifier = Modifier.weight(1f))
        HabitTrailing(summary = summary, onCountChange = onCountChange)
    }
}

@Composable
private fun HabitSummaryText(
    summary: HabitDaySummary,
    modifier: Modifier = Modifier,
) {
    val habit = summary.habit

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = habit.displayName(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        StreakLine(summary = summary)
    }
}

@Composable
private fun StreakLine(summary: HabitDaySummary) {
    val frequency = summary.habit.frequency
    val hasStreak = summary.currentStreak > 0
    val color =
        if (hasStreak) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val text =
        listOfNotNull(
            streakText(summary.currentStreak),
            frequency.periodProgressText(summary.periodCount),
        ).joinToString(separator = SEPARATOR)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (hasStreak) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp),
            )
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private const val SEPARATOR = " · "

@PreviewLightDark
@Composable
private fun HabitItemPreview() {
    WellyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PreviewHabitSummaries.forEach { summary ->
                HabitItem(summary = summary, onClick = {}, onCountChange = {})
            }
        }
    }
}
