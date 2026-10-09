package com.jula1717.welly.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.presentation.util.titleResId
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ActivityTypeChip(
    type: ActivityType,
    modifier: Modifier = Modifier,
) {
    val (containerColor, contentColor) = when (type) {
        ActivityType.Cardio ->
            MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        ActivityType.Strength ->
            MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    }

    LabelChip(
        label = stringResource(type.titleResId),
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun ActivityTypeChipPreview() {
    WellyTheme {
        Row {
            ActivityTypeChip(type = ActivityType.Cardio, modifier = Modifier.padding(4.dp))
            ActivityTypeChip(type = ActivityType.Strength, modifier = Modifier.padding(4.dp))
        }
    }
}
