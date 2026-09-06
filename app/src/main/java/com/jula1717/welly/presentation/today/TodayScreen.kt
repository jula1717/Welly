package com.jula1717.welly.presentation.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.presentation.components.MacroType
import com.jula1717.welly.presentation.components.WellyDatePickerDialog
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate

@Composable
fun TodayDestination(
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: (() -> Unit),
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.onScreenResumed()
        onPauseOrDispose {}
    }

    TodayScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onAddMeal = onAddMeal,
        onAddDrink = onAddDrink,
        onOpenProfile = onOpenProfile,
        onOpenSettings = onOpenSettings,
        modifier = modifier,
    )
}

@Composable
internal fun TodayScreen(
    state: TodayUiState,
    onEvent: (TodayUiEvent) -> Unit,
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var fabExpanded by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        WellyDatePickerDialog(
            initialDate = state.date,
            maxDate = state.maxSelectableDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                onEvent(TodayUiEvent.OnDateSelected(date))
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
                onPreviousDay = { onEvent(TodayUiEvent.OnPreviousDay) },
                onNextDay = { onEvent(TodayUiEvent.OnNextDay) },
                onDateClick = { showDatePicker = true },
                onOpenProfile = onOpenProfile,
                onOpenSettings = onOpenSettings,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    DailyCalorieCard(
                        calories = state.totals.calories,
                        targetMin = state.targets.calorieTargetKcalMin,
                        targetMax = state.targets.calorieTargetKcalMax,
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MacroTargetCard(
                                type = MacroType.PROTEIN,
                                grams = state.totals.protein,
                                targetMin = state.targets.proteinGramsMin,
                                targetMax = state.targets.proteinGramsMax,
                                modifier = Modifier.weight(1f),
                            )
                            MacroTargetCard(
                                type = MacroType.CARBS,
                                grams = state.totals.carbs,
                                targetMin = state.targets.carbsGramsMin,
                                targetMax = state.targets.carbsGramsMax,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MacroTargetCard(
                                type = MacroType.FAT,
                                grams = state.totals.fat,
                                targetMin = state.targets.fatGramsMin,
                                targetMax = state.targets.fatGramsMax,
                                modifier = Modifier.weight(1f),
                            )
                            MacroTargetCard(
                                type = MacroType.FIBER,
                                grams = state.totals.fiber,
                                targetMin = state.targets.fiberGramsMin,
                                targetMax = state.targets.fiberGramsMax,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                item {
                    WaterCard(
                        currentMl = state.totals.hydrationMl,
                        targetMl = state.targets.hydrationMlMax,
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.today_entries_count, state.entries.size),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                    )
                }

                items(state.entries, key = { it.key }) { entry ->
                    DailyEntryItem(entry = entry)
                }
            }
        }

        AddEntryScrim(
            visible = fabExpanded,
            onDismiss = { fabExpanded = false },
        )

        AddEntryFab(
            expanded = fabExpanded,
            onExpandedChange = { fabExpanded = it },
            onAddMeal = onAddMeal,
            onAddDrink = onAddDrink,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun TodayScreenPreview() {
    WellyTheme {
        TodayScreen(
            state = TodayUiState(
                date = LocalDate.now(),
                maxSelectableDate = LocalDate.now(),
                targets = PREVIEW_TARGETS,
            ),
            onEvent = {},
            onAddMeal = {},
            onAddDrink = {},
            onOpenProfile = {},
            onOpenSettings = {},
        )
    }
}

private val PREVIEW_TARGETS = DailyTargets(
    bmrKcal = 1500,
    tdeeKcal = 1900,
    calorieTargetKcalMin = 1800,
    calorieTargetKcalMax = 2000,
    proteinGramsMin = 100,
    proteinGramsMax = 120,
    carbsGramsMin = 200,
    carbsGramsMax = 235,
    fatGramsMin = 55,
    fatGramsMax = 70,
    fiberGramsMin = 25,
    fiberGramsMax = 35,
    hydrationMlMin = 1800,
    hydrationMlMax = 2200,
)
