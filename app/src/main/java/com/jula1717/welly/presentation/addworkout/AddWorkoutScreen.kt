package com.jula1717.welly.presentation.addworkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.jula1717.welly.presentation.components.DateTimeRow
import com.jula1717.welly.presentation.components.SaveButton
import com.jula1717.welly.presentation.components.WellyDatePickerDialog
import com.jula1717.welly.presentation.components.WellyTimePickerDialog
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddWorkoutDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddWorkoutViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddWorkoutEffect.NavigateBack -> onBack()
            }
        }
    }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        WellyDatePickerDialog(
            initialDate = state.date,
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                viewModel.onEvent(AddWorkoutUiEvent.OnDateChanged(date))
                showDatePicker = false
            },
            modifier = Modifier.padding(horizontal = 32.dp),
        )
    }

    if (showTimePicker) {
        WellyTimePickerDialog(
            initialTime = state.time,
            onDismiss = { showTimePicker = false },
            onConfirm = { time ->
                viewModel.onEvent(AddWorkoutUiEvent.OnTimeChanged(time))
                showTimePicker = false
            },
        )
    }

    AddWorkoutScreen(
        state = state,
        onBack = onBack,
        onShowDatePicker = { showDatePicker = true },
        onShowTimePicker = { showTimePicker = true },
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun AddWorkoutScreen(
    state: AddWorkoutUiState,
    onBack: () -> Unit,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onEvent: (AddWorkoutUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.add_workout_title, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            DateTimeRow(
                date = state.date,
                time = state.time,
                onDateClick = onShowDatePicker,
                onTimeClick = onShowTimePicker,
                modifier = Modifier.padding(top = 4.dp),
            )

            if (state.activities.isEmpty()) {
                Text(
                    text = stringResource(R.string.add_workout_no_activities),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                ActivitySelection(
                    typeFilter = state.typeFilter,
                    activities = state.visibleActivities,
                    selectedActivityId = state.selectedActivityId,
                    onTypeFilterChanged = { onEvent(AddWorkoutUiEvent.OnTypeFilterChanged(it)) },
                    onActivitySelected = { onEvent(AddWorkoutUiEvent.OnActivitySelected(it)) },
                )
            }
        }

        SaveButton(
            enabled = state.canSave,
            onClick = { onEvent(AddWorkoutUiEvent.OnSave) },
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun AddWorkoutScreenPreview() {
    WellyTheme {
        AddWorkoutScreen(
            state = AddWorkoutUiState(
                date = LocalDate.of(2026, 10, 9),
                time = LocalTime.of(14, 30),
                activities = listOf(
                    Activity(id = 1, name = "Jogging", type = ActivityType.Cardio),
                    Activity(id = 2, name = "Circuit training", type = ActivityType.Cardio),
                    Activity(id = 3, name = "Kettlebell", type = ActivityType.Strength),
                ),
                selectedActivityId = 2,
            ),
            onBack = {},
            onShowDatePicker = {},
            onShowTimePicker = {},
            onEvent = {},
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun AddWorkoutScreenEmptyPreview() {
    WellyTheme {
        AddWorkoutScreen(
            state = AddWorkoutUiState(date = LocalDate.of(2026, 10, 9), time = LocalTime.of(14, 30)),
            onBack = {},
            onShowDatePicker = {},
            onShowTimePicker = {},
            onEvent = {},
        )
    }
}
