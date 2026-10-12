package com.jula1717.welly.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.presentation.components.AddFab
import com.jula1717.welly.presentation.components.DayHeader
import com.jula1717.welly.presentation.components.WellyDatePickerDialog
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun HabitsDestination(
    onOpenHabit: (Long) -> Unit,
    onAddHabit: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.onScreenResumed()
        onPauseOrDispose {}
    }

    HabitsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenHabit = onOpenHabit,
        onAddHabit = onAddHabit,
        onOpenProfile = onOpenProfile,
        onOpenSettings = onOpenSettings,
        modifier = modifier,
    )
}

@Composable
internal fun HabitsScreen(
    state: HabitsUiState,
    onEvent: (HabitsUiEvent) -> Unit,
    onOpenHabit: (Long) -> Unit,
    onAddHabit: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        WellyDatePickerDialog(
            initialDate = state.date,
            maxDate = state.maxSelectableDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                onEvent(HabitsUiEvent.OnDateSelected(date))
                showDatePicker = false
            },
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DayHeader(
                date = state.date,
                canGoToNextDay = state.canGoToNextDay,
                onPreviousDay = { onEvent(HabitsUiEvent.OnPreviousDay) },
                onNextDay = { onEvent(HabitsUiEvent.OnNextDay) },
                onDateClick = { showDatePicker = true },
                onOpenProfile = onOpenProfile,
                onOpenSettings = onOpenSettings,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.habits, key = { it.habit.id }) { summary ->
                    HabitItem(
                        summary = summary,
                        onClick = { onOpenHabit(summary.habit.id) },
                        onCountChange = { count ->
                            onEvent(HabitsUiEvent.OnCountChanged(summary.habit.id, count))
                        },
                    )
                }
            }
        }

        AddFab(
            contentDescription = stringResource(R.string.habits_add),
            onClick = onAddHabit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun HabitsScreenPreview() {
    WellyTheme {
        HabitsScreen(
            state = HabitsUiState(
                date = PreviewDate,
                maxSelectableDate = PreviewDate,
                habits = PreviewHabitSummaries,
            ),
            onEvent = {},
            onOpenHabit = {},
            onAddHabit = {},
            onOpenProfile = {},
            onOpenSettings = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun HabitsScreenEmptyPreview() {
    WellyTheme {
        HabitsScreen(
            state = HabitsUiState(
                date = PreviewDate,
                maxSelectableDate = PreviewDate,
            ),
            onEvent = {},
            onOpenHabit = {},
            onAddHabit = {},
            onOpenProfile = {},
            onOpenSettings = {},
        )
    }
}
