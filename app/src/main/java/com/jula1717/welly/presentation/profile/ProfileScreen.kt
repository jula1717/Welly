package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.presentation.components.SaveButton
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate

@Composable
fun ProfileDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateBack -> onBack()
            }
        }
    }

    ProfileScreen(
        state = state,
        maxDateOfBirth = viewModel.maxDateOfBirth,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
internal fun ProfileScreen(
    state: ProfileUiState,
    maxDateOfBirth: LocalDate,
    onEvent: (ProfileUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.profile_settings_title, onBack = onBack)

        if (!state.isLoaded) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            ProfileForm(
                state = state,
                maxDateOfBirth = maxDateOfBirth,
                onEvent = onEvent,
                modifier = Modifier.weight(1f),
            )

            if (state.loadFailed || state.saveFailed) {
                FormError(
                    text = stringResource(
                        if (state.saveFailed) {
                            R.string.profile_save_failed
                        } else {
                            R.string.profile_load_failed
                        },
                    ),
                )
            }

            SaveButton(
                enabled = state.canSave,
                onClick = { onEvent(ProfileUiEvent.OnSave) },
            )
        }
    }
}

@Composable
private fun ProfileForm(
    state: ProfileUiState,
    maxDateOfBirth: LocalDate,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        FormIntro()

        LabeledSection(stringResource(R.string.profile_dob_label)) {
            BirthDatePicker(
                dateOfBirth = state.dateOfBirth ?: ProfileUiState.DEFAULT_DATE_OF_BIRTH,
                maxDate = maxDateOfBirth,
                onDateChange = { onEvent(ProfileUiEvent.OnDateOfBirthChanged(it)) },
            )
        }

        LabeledSection(stringResource(R.string.profile_height_label)) {
            RulerField(
                label = stringResource(R.string.profile_height_label),
                value = state.heightCm ?: ProfileUiState.DEFAULT_HEIGHT_CM,
                unit = stringResource(R.string.profile_height_unit),
                range = ProfileUiState.MIN_HEIGHT_CM..ProfileUiState.MAX_HEIGHT_CM,
                onValueChange = { onEvent(ProfileUiEvent.OnHeightChanged(it)) },
            )
        }

        LabeledSection(stringResource(R.string.profile_weight_label)) {
            WeightField(
                weightKg = state.weightKg,
                range = ProfileUiState.MIN_WEIGHT_KG..ProfileUiState.MAX_WEIGHT_KG,
                unit = stringResource(R.string.profile_weight_unit),
                onWeightChange = { onEvent(ProfileUiEvent.OnWeightChanged(it)) },
            )
        }

        SexSection(
            selected = state.sex,
            onSelected = { onEvent(ProfileUiEvent.OnSexChanged(it)) },
        )

        ActivitySection(
            selected = state.activityLevel,
            onSelected = { onEvent(ProfileUiEvent.OnActivityLevelChanged(it)) },
        )

        GoalSection(
            selected = state.goal,
            onSelected = { onEvent(ProfileUiEvent.OnGoalChanged(it)) },
        )
    }
}

@Composable
private fun FormIntro() {
    Text(
        text = stringResource(R.string.profile_settings_intro),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp),
    )
}

@Composable
private fun FormError(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    )
}

@PreviewLightDark
@Composable
private fun ProfileScreenLoadingPreview() {
    WellyTheme {
        ProfileScreen(
            state = ProfileUiState.EMPTY,
            maxDateOfBirth = LocalDate.now(),
            onEvent = {},
            onBack = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun ProfileScreenEmptyPreview() {
    WellyTheme {
        ProfileScreen(
            state = ProfileUiState.EMPTY.copy(isLoaded = true),
            maxDateOfBirth = LocalDate.now(),
            onEvent = {},
            onBack = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun ProfileScreenFilledPreview() {
    WellyTheme {
        ProfileScreen(
            state = ProfileUiState.EMPTY.copy(
                isLoaded = true,
                dateOfBirth = LocalDate.of(1996, 6, 12),
                heightCm = 178,
                weightKg = 72.0,
                sex = BiologicalSex.Female,
                activityLevel = ActivityLevel.Moderate,
                goal = NutritionGoal.Maintain,
            ),
            maxDateOfBirth = LocalDate.now(),
            onEvent = {},
            onBack = {},
        )
    }
}
