package com.jula1717.welly.presentation.addmeal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.presentation.components.DateTimeCard
import com.jula1717.welly.presentation.components.MealTypeChip
import com.jula1717.welly.presentation.components.SaveButton
import com.jula1717.welly.presentation.components.WellyDatePickerDialog
import com.jula1717.welly.presentation.components.WellyTimePickerDialog
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.presentation.util.ClearFocusOnResume
import com.jula1717.welly.presentation.util.DATE_DISPLAY_FORMAT
import com.jula1717.welly.presentation.util.TIME_DISPLAY_FORMAT
import com.jula1717.welly.presentation.util.titleResId
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddMealDestination(
    onBack: () -> Unit,
    viewModel: AddMealViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // TODO: replace deprecated LocalClipboardManager with LocalClipboard.
    val clipboardManager = LocalClipboardManager.current

    ClearFocusOnResume()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddMealEffect.NavigateBack -> onBack()
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
                viewModel.onEvent(AddMealUiEvent.OnDateChanged(date))
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
                viewModel.onEvent(AddMealUiEvent.OnTimeChanged(time))
                showTimePicker = false
            },
        )
    }

    AddMealScreen(
        selectedType = state.type,
        description = state.description,
        date = state.date,
        time = state.time,
        macros = state.macros,
        macrosError = state.macrosError,
        canSave = state.canSave,
        onBack = onBack,
        onShowDatePicker = { showDatePicker = true },
        onShowTimePicker = { showTimePicker = true },
        onEvent = viewModel::onEvent,
        onCopyPrompt = {
            clipboardManager.setText(AnnotatedString(state.generatedPrompt))
            viewModel.onEvent(AddMealUiEvent.OnCopyPromptClicked)
        },
        onPasteJson = {
            viewModel.onEvent(
                AddMealUiEvent.OnMacrosJsonChanged(clipboardManager.getText()?.text.orEmpty()),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun AddMealScreen(
    selectedType: MealType,
    description: String,
    date: LocalDate,
    time: LocalTime,
    macros: MealMacros?,
    macrosError: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onEvent: (AddMealUiEvent) -> Unit,
    onCopyPrompt: () -> Unit,
    onPasteJson: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.add_meal_title, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp),
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MealType.entries.forEach { type ->
                    MealTypeChip(
                        label = stringResource(type.titleResId),
                        selected = type == selectedType,
                        onClick = { onEvent(AddMealUiEvent.OnTypeChanged(type)) },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                DateTimeCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.add_meal_date_label),
                    value = date.format(DATE_DISPLAY_FORMAT),
                    icon = Icons.Default.CalendarToday,
                    onClick = onShowDatePicker,
                )
                DateTimeCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.add_meal_time_label),
                    value = time.format(TIME_DISPLAY_FORMAT),
                    icon = Icons.Default.Schedule,
                    onClick = onShowTimePicker,
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = { onEvent(AddMealUiEvent.OnDescriptionChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = { Text(stringResource(R.string.add_meal_description_label)) },
                minLines = 3,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                ),
            )

            AiMacroCard(
                modifier = Modifier.padding(top = 16.dp),
                macros = macros,
                macrosError = macrosError,
                onCopyPrompt = onCopyPrompt,
                onPasteJson = onPasteJson,
            )
        }

        SaveButton(
            enabled = canSave,
            onClick = { onEvent(AddMealUiEvent.OnSave) },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AddMealScreenPreview() {
    WellyTheme {
        AddMealScreen(
            selectedType = MealType.Breakfast,
            description = "",
            date = LocalDate.now(),
            time = LocalTime.now(),
            macros = null,
            macrosError = false,
            canSave = false,
            onBack = {},
            onShowDatePicker = {},
            onShowTimePicker = {},
            onEvent = {},
            onCopyPrompt = {},
            onPasteJson = {},
        )
    }
}
