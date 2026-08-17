package com.jula1717.welly.presentation.adddrink

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.presentation.components.AiMacroCard
import com.jula1717.welly.presentation.components.AmountStepper
import com.jula1717.welly.presentation.components.DateTimeCard
import com.jula1717.welly.presentation.components.SaveButton
import com.jula1717.welly.presentation.components.SegmentedToggle
import com.jula1717.welly.presentation.components.WellyDatePickerDialog
import com.jula1717.welly.presentation.components.WellyTimePickerDialog
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.presentation.util.ClearFocusOnResume
import com.jula1717.welly.presentation.util.DATE_DISPLAY_FORMAT
import com.jula1717.welly.presentation.util.TIME_DISPLAY_FORMAT
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddDrinkDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDrinkViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    ClearFocusOnResume()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddDrinkEffect.NavigateBack -> onBack()
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
                viewModel.onEvent(AddDrinkUiEvent.OnDateChanged(date))
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
                viewModel.onEvent(AddDrinkUiEvent.OnTimeChanged(time))
                showTimePicker = false
            },
        )
    }

    AddDrinkScreen(
        calorieType = state.calorieType,
        amountMl = state.amountMl,
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
            viewModel.onEvent(AddDrinkUiEvent.OnCopyPromptClicked)
        },
        onPasteJson = {
            viewModel.onEvent(
                AddDrinkUiEvent.OnMacrosJsonChanged(clipboardManager.getText()?.text.orEmpty()),
            )
        },
        modifier = modifier,
    )
}

@Composable
internal fun AddDrinkScreen(
    calorieType: DrinkCalorieType,
    amountMl: Int,
    description: String,
    date: LocalDate,
    time: LocalTime,
    macros: MealMacros?,
    macrosError: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onShowDatePicker: () -> Unit,
    onShowTimePicker: () -> Unit,
    onEvent: (AddDrinkUiEvent) -> Unit,
    onCopyPrompt: () -> Unit,
    onPasteJson: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.add_drink_title, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp),
        ) {
            SegmentedToggle(
                options = listOf(
                    stringResource(R.string.add_drink_calorie_caloric),
                    stringResource(R.string.add_drink_calorie_non_caloric),
                ),
                selectedIndex = if (calorieType == DrinkCalorieType.Caloric) 0 else 1,
                onOptionSelected = { index ->
                    val type = if (index == 0) DrinkCalorieType.Caloric else DrinkCalorieType.NonCaloric
                    onEvent(AddDrinkUiEvent.OnCalorieTypeChanged(type))
                },
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                DateTimeCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.date_label),
                    value = date.format(DATE_DISPLAY_FORMAT),
                    icon = Icons.Default.CalendarToday,
                    onClick = onShowDatePicker,
                )
                DateTimeCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.time_label),
                    value = time.format(TIME_DISPLAY_FORMAT),
                    icon = Icons.Default.Schedule,
                    onClick = onShowTimePicker,
                )
            }

            AmountStepper(
                amountMl = amountMl,
                onAmountMlChange = { onEvent(AddDrinkUiEvent.OnAmountChanged(it)) },
                hint = stringResource(R.string.add_drink_amount_hint),
                unitLabel = stringResource(R.string.unit_ml),
                minAmountMl = AddDrinkUiState.MIN_AMOUNT_ML,
                maxAmountMl = AddDrinkUiState.MAX_AMOUNT_ML,
                modifier = Modifier.padding(top = 14.dp),
            )

            OutlinedTextField(
                value = description,
                onValueChange = { onEvent(AddDrinkUiEvent.OnDescriptionChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                label = {
                    val labelRes = if (calorieType == DrinkCalorieType.Caloric) {
                        R.string.add_drink_description_label
                    } else {
                        R.string.add_drink_description_label_optional
                    }
                    Text(stringResource(labelRes))
                },
                minLines = 2,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
            )

            if (calorieType == DrinkCalorieType.Caloric) {
                AiMacroCard(
                    modifier = Modifier.padding(top = 16.dp),
                    macros = macros,
                    macrosError = macrosError,
                    onCopyPrompt = onCopyPrompt,
                    onPasteJson = onPasteJson,
                )
            }
        }

        SaveButton(
            enabled = canSave,
            onClick = { onEvent(AddDrinkUiEvent.OnSave) },
        )
    }
}

@PreviewLightDark
@Composable
private fun AddDrinkScreenCaloricPreview() {
    WellyTheme {
        AddDrinkScreen(
            calorieType = DrinkCalorieType.Caloric,
            amountMl = 330,
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

@PreviewLightDark
@Composable
private fun AddDrinkScreenNonCaloricPreview() {
    WellyTheme {
        AddDrinkScreen(
            calorieType = DrinkCalorieType.NonCaloric,
            amountMl = 330,
            description = "",
            date = LocalDate.now(),
            time = LocalTime.now(),
            macros = null,
            macrosError = false,
            canSave = true,
            onBack = {},
            onShowDatePicker = {},
            onShowTimePicker = {},
            onEvent = {},
            onCopyPrompt = {},
            onPasteJson = {},
        )
    }
}
