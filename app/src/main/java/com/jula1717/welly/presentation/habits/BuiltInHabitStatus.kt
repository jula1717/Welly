package com.jula1717.welly.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.presentation.util.descriptionResId
import com.jula1717.welly.presentation.util.icon
import com.jula1717.welly.ui.theme.WellyTheme

/**
 * Shows whether a built-in habit is met today. It is deliberately not a checkbox: built-in habits cannot be ticked
 * by hand, so it shows the habit's own icon, filled and with a small tick once the goal is reached.
 */
@Composable
internal fun BuiltInHabitStatus(
    builtIn: BuiltInHabit,
    status: HabitDayStatus,
    modifier: Modifier = Modifier,
) {
    BuiltInHabitStatus(icon = builtIn.icon, status = status, modifier = modifier)
}

@Composable
internal fun BuiltInHabitStatus(
    icon: ImageVector,
    status: HabitDayStatus,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val (containerColor, iconColor) =
        when (status) {
            HabitDayStatus.Done -> colors.primaryContainer to colors.onPrimaryContainer
            HabitDayStatus.Covered -> colors.secondaryContainer to colors.onSecondaryContainer
            HabitDayStatus.None -> colors.surfaceContainerLowest to colors.onSurfaceVariant
        }
    val description = stringResource(status.descriptionResId)

    Box(
        modifier = modifier
            .size(40.dp)
            .semantics { contentDescription = description },
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(containerColor)
                .border(
                    width = if (status == HabitDayStatus.None) 2.dp else 0.dp,
                    color = colors.outlineVariant,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp),
            )
        }

        if (status != HabitDayStatus.None) {
            TickBadge(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
private fun TickBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, MaterialTheme.colorScheme.surfaceContainer, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(10.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun BuiltInHabitStatusPreview() {
    WellyTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(12.dp),
        ) {
            BuiltInHabit.entries.forEachIndexed { index, builtIn ->
                BuiltInHabitStatus(
                    builtIn = builtIn,
                    status = HabitDayStatus.entries[index % HabitDayStatus.entries.size],
                    modifier = Modifier.padding(4.dp),
                )
            }
        }
    }
}
