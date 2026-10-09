package com.jula1717.welly.presentation.addworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.presentation.components.ActivityItem
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ActivityPicker(
    activities: List<Activity>,
    selectedActivityId: Long?,
    onActivitySelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        activities.forEach { activity ->
            ActivityItem(
                activity = activity,
                selected = activity.id == selectedActivityId,
                onClick = { onActivitySelected(activity.id) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ActivityPickerPreview() {
    WellyTheme {
        ActivityPicker(
            activities = listOf(
                Activity(id = 1, name = "Jogging", type = ActivityType.Cardio),
                Activity(id = 2, name = "Kettlebell", type = ActivityType.Strength),
            ),
            selectedActivityId = 1,
            onActivitySelected = {},
        )
    }
}
