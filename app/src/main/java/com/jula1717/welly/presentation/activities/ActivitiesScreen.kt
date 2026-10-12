package com.jula1717.welly.presentation.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.presentation.components.ActivityItem
import com.jula1717.welly.presentation.components.DeleteConfirmationDialog
import com.jula1717.welly.presentation.components.PrimaryButton
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun ActivitiesDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActivitiesViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ActivitiesScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun ActivitiesScreen(
    state: ActivitiesUiState,
    onBack: () -> Unit,
    onEvent: (ActivitiesUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state.form?.let { form ->
        ActivityFormSheet(form = form, onEvent = onEvent)
    }

    state.pendingDelete?.let { activity ->
        DeleteConfirmationDialog(
            title = stringResource(R.string.activities_delete_title, activity.name),
            message = stringResource(R.string.activities_delete_body),
            onConfirm = { onEvent(ActivitiesUiEvent.OnDeleteConfirmed) },
            onDismiss = { onEvent(ActivitiesUiEvent.OnDeleteDismissed) },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.activities_title, onBack = onBack)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = stringResource(R.string.activities_intro),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            if (state.activities.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.activities_empty),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
            } else {
                items(state.activities, key = { it.id }) { activity ->
                    ActivityItem(
                        activity = activity,
                        onClick = { onEvent(ActivitiesUiEvent.OnEditClicked(activity)) },
                    )
                }
            }
        }

        PrimaryButton(
            text = stringResource(R.string.activities_add),
            enabled = true,
            onClick = { onEvent(ActivitiesUiEvent.OnAddClicked) },
        )
    }
}

@PreviewLightDark
@Composable
private fun ActivitiesScreenPreview() {
    WellyTheme {
        ActivitiesScreen(
            state = ActivitiesUiState(
                activities = listOf(
                    Activity(id = 1, name = "Jogging", type = ActivityType.Cardio),
                    Activity(id = 2, name = "Gym — upper body", type = ActivityType.Strength),
                ),
            ),
            onBack = {},
            onEvent = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun ActivitiesScreenEmptyPreview() {
    WellyTheme {
        ActivitiesScreen(
            state = ActivitiesUiState(),
            onBack = {},
            onEvent = {},
        )
    }
}
