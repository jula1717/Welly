package com.jula1717.welly.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.presentation.util.descriptionResId
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun HabitCheckButton(
    status: HabitDayStatus,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    val (containerColor, borderColor) =
        when (status) {
            HabitDayStatus.Done -> colors.primaryContainer to colors.primaryContainer
            HabitDayStatus.Covered -> colors.secondaryContainer to colors.inversePrimary
            HabitDayStatus.None -> colors.surfaceContainerLowest to colors.outlineVariant
        }
    val description = stringResource(status.descriptionResId)

    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(if (status == HabitDayStatus.None) 2.dp else 1.dp, borderColor, CircleShape)
            .then(if (onClick != null) Modifier.clickable(role = Role.Checkbox, onClick = onClick) else Modifier)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        if (status != HabitDayStatus.None) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (status == HabitDayStatus.Done) colors.onPrimaryContainer else colors.onSecondaryContainer,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HabitCheckButtonPreview() {
    WellyTheme {
        Row(modifier = Modifier.padding(8.dp)) {
            HabitDayStatus.entries.forEach { status ->
                HabitCheckButton(status = status, onClick = {}, modifier = Modifier.padding(4.dp))
            }
        }
    }
}
