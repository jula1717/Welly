package com.jula1717.welly.presentation.addworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ActivitySelection(
    typeFilter: ActivityType?,
    activities: List<Activity>,
    selectedActivityId: Long?,
    onTypeFilterChanged: (ActivityType?) -> Unit,
    onActivitySelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        FormLabel(stringResource(R.string.add_workout_type_label))
        ActivityTypeFilter(
            selected = typeFilter,
            onSelected = onTypeFilterChanged,
        )

        FormLabel(stringResource(R.string.add_workout_activity_label))
        if (activities.isEmpty()) {
            Text(
                text = stringResource(R.string.add_workout_no_activities_of_type),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            ActivityPicker(
                activities = activities,
                selectedActivityId = selectedActivityId,
                onActivitySelected = onActivitySelected,
            )
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp),
    )
}

@PreviewLightDark
@Composable
private fun ActivitySelectionPreview() {
    WellyTheme {
        ActivitySelection(
            typeFilter = null,
            activities = listOf(
                Activity(id = 1, name = "Jogging", type = ActivityType.Cardio),
                Activity(id = 2, name = "Kettlebell", type = ActivityType.Strength),
            ),
            selectedActivityId = 1,
            onTypeFilterChanged = {},
            onActivitySelected = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun ActivitySelectionNoMatchesPreview() {
    WellyTheme {
        ActivitySelection(
            typeFilter = ActivityType.Strength,
            activities = emptyList(),
            selectedActivityId = null,
            onTypeFilterChanged = {},
            onActivitySelected = {},
        )
    }
}
