package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.presentation.util.descriptionResId
import com.jula1717.welly.presentation.util.icon
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun HabitCalendarDay(
    dayOfMonth: Int,
    state: HabitDayState?,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val (containerColor, contentColor) =
        when (state?.status) {
            HabitDayStatus.Done -> colors.primaryContainer to colors.onPrimaryContainer
            HabitDayStatus.Covered -> colors.secondaryContainer to colors.onSecondaryContainer
            HabitDayStatus.None -> Color.Transparent to colors.onSurface
            null -> Color.Transparent to colors.onSurfaceVariant.copy(alpha = FUTURE_ALPHA)
        }
    val description = state?.let { "$dayOfMonth, ${stringResource(it.status.descriptionResId)}" } ?: "$dayOfMonth"

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(containerColor)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        (state?.detail as? HabitDayDetail.Workouts)?.let { workouts ->
            WorkoutIcons(types = workouts.activityTypes, tint = contentColor.copy(alpha = ICON_ALPHA))
        }
        Text(
            text = dayOfMonth.toString(),
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
        )
    }
}

@Composable
private fun WorkoutIcons(
    types: Set<ActivityType>,
    tint: Color,
) {
    val sorted = types.sortedBy { it.ordinal }
    val iconSize = if (sorted.size > 1) 17.dp else 24.dp
    val shift = if (sorted.size > 1) 6.dp else 0.dp

    sorted.forEachIndexed { index, type ->
        val direction = if (index == 0) -1 else 1
        Icon(
            imageVector = type.icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(iconSize)
                .offset(x = shift * direction, y = shift * direction / 2),
        )
    }
}

private const val FUTURE_ALPHA = 0.5f
private const val ICON_ALPHA = 0.35f

@PreviewLightDark
@Composable
private fun HabitCalendarDayPreview() {
    WellyTheme {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            HabitCalendarDay(dayOfMonth = 1, state = HabitDayState(HabitDayStatus.Done, count = 1))
            HabitCalendarDay(dayOfMonth = 2, state = HabitDayState(HabitDayStatus.Covered))
            HabitCalendarDay(dayOfMonth = 3, state = HabitDayState(HabitDayStatus.None))
            HabitCalendarDay(dayOfMonth = 4, state = null)
            HabitCalendarDay(
                dayOfMonth = 5,
                state = HabitDayState(
                    HabitDayStatus.Done,
                    count = 1,
                    detail = HabitDayDetail.Workouts(setOf(ActivityType.Cardio)),
                ),
            )
            HabitCalendarDay(
                dayOfMonth = 6,
                state = HabitDayState(
                    HabitDayStatus.Done,
                    count = 2,
                    detail = HabitDayDetail.Workouts(setOf(ActivityType.Cardio, ActivityType.Strength)),
                ),
            )
        }
    }
}
