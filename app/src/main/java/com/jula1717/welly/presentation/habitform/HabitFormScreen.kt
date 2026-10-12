package com.jula1717.welly.presentation.habitform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.presentation.components.CircleIconButton
import com.jula1717.welly.presentation.components.DeleteConfirmationDialog
import com.jula1717.welly.presentation.components.LabeledSection
import com.jula1717.welly.presentation.components.PrimaryButton
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.presentation.util.label
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun HabitFormDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitFormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HabitFormEffect.NavigateBack -> onBack()
            }
        }
    }

    HabitFormScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun HabitFormScreen(
    state: HabitFormUiState,
    onBack: () -> Unit,
    onEvent: (HabitFormUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            title = stringResource(R.string.habit_delete_title, state.name),
            message = stringResource(R.string.habit_delete_body),
            onConfirm = { onEvent(HabitFormUiEvent.OnDeleteConfirmed) },
            onDismiss = { onEvent(HabitFormUiEvent.OnDeleteDismissed) },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(
            titleRes = if (state.isNew) R.string.habit_form_new_title else R.string.habit_form_edit_title,
            onBack = onBack,
            actions = {
                if (state.canDelete) {
                    CircleIconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.habit_cd_delete),
                        onClick = { onEvent(HabitFormUiEvent.OnDeleteClicked) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            },
        )

        HabitFormFieldsColumn(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.weight(1f),
        )

        PrimaryButton(
            text = stringResource(if (state.isNew) R.string.habit_form_add else R.string.save),
            enabled = state.canSave && !state.isSaving,
            onClick = { onEvent(HabitFormUiEvent.OnSave) },
        )
    }
}

@Composable
private fun HabitFormFieldsColumn(
    state: HabitFormUiState,
    onEvent: (HabitFormUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (!state.isBuiltIn) {
            LabeledSection(stringResource(R.string.habit_form_name_label)) {
                HabitNameField(
                    name = state.name,
                    onNameChange = { onEvent(HabitFormUiEvent.OnNameChanged(it)) },
                )
            }
        }

        LabeledSection(stringResource(R.string.habit_form_points_label)) {
            HabitPointsField(
                points = state.points,
                onPointsChange = { onEvent(HabitFormUiEvent.OnPointsChanged(it)) },
            )
        }

        LabeledSection(stringResource(R.string.habit_form_frequency_label)) {
            if (state.isBuiltIn) {
                FixedFrequencyNote(state = state)
            } else {
                FrequencySection(input = state.frequency, onEvent = onEvent)
            }
        }

        if (state.quotaTooHigh) {
            Text(
                text = stringResource(R.string.habit_form_quota_too_high),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun FixedFrequencyNote(state: HabitFormUiState) {
    val frequency = state.frequency.toFrequency()
    val description = frequency?.label().orEmpty()

    Text(
        text = stringResource(R.string.habit_form_frequency_fixed, description),
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private val PREVIEW_FREQUENCY = HabitFrequency(times = 10, periodLength = 14, periodUnit = HabitPeriodUnit.Day)

@PreviewLightDark
@Composable
private fun HabitFormScreenNewPreview() {
    WellyTheme {
        HabitFormScreen(
            state = HabitFormUiState(
                name = "Brushing teeth",
                frequency = FrequencyInput().withValue(FrequencyField.TimesPerDay, 2),
                canSave = true,
            ),
            onBack = {},
            onEvent = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun HabitFormScreenEditPreview() {
    WellyTheme {
        HabitFormScreen(
            state = HabitFormUiState(
                habitId = 1,
                name = "Cooking at home",
                frequency = FrequencyInput.from(PREVIEW_FREQUENCY),
                canSave = true,
            ),
            onBack = {},
            onEvent = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun HabitFormScreenBuiltInPreview() {
    WellyTheme {
        HabitFormScreen(
            state = HabitFormUiState(
                habitId = 2,
                builtIn = BuiltInHabit.Hydration,
                frequency = FrequencyInput(FrequencyOption.Daily),
                canSave = true,
            ),
            onBack = {},
            onEvent = {},
        )
    }
}
