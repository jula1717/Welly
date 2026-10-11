package com.jula1717.welly.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.domain.usecase.CalculateDailyIntakeTotalsUseCase
import com.jula1717.welly.domain.usecase.CalculateDailyTargetsUseCase
import com.jula1717.welly.domain.usecase.GetDrinksForDayUseCase
import com.jula1717.welly.domain.usecase.GetMealsForDayUseCase
import com.jula1717.welly.domain.usecase.GetWorkoutsForDayUseCase
import com.jula1717.welly.domain.usecase.ObserveUserProfileUseCase
import com.jula1717.welly.presentation.util.canGoToNextDay
import com.jula1717.welly.presentation.util.coerceNotFuture
import com.jula1717.welly.presentation.util.nextDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel
    @Inject
    constructor(
        getMealsForDayUseCase: GetMealsForDayUseCase,
        getDrinksForDayUseCase: GetDrinksForDayUseCase,
        getWorkoutsForDayUseCase: GetWorkoutsForDayUseCase,
        observeUserProfileUseCase: ObserveUserProfileUseCase,
        calculateDailyTargetsUseCase: CalculateDailyTargetsUseCase,
        private val calculateDailyIntakeTotals: CalculateDailyIntakeTotalsUseCase,
        private val clock: Clock,
    ) : ViewModel() {
        private fun today(): LocalDate = LocalDate.now(clock)

        /** The day the user is currently looking at. */
        private val selectedDate = MutableStateFlow(today())

        /** "Today", re-read on every screen resume so a midnight rollover is picked up. */
        private val currentDay = MutableStateFlow(today())

        private val initialTargets: DailyTargets =
            calculateDailyTargetsUseCase(UserProfile.default(today()))
        private val targets: Flow<DailyTargets> =
            observeUserProfileUseCase().map { profile ->
                calculateDailyTargetsUseCase(profile ?: UserProfile.default(today()))
            }

        private val dayEntries: Flow<DayEntries> =
            selectedDate.flatMapLatest { day ->
                combine(
                    getMealsForDayUseCase(day),
                    getDrinksForDayUseCase(day),
                    getWorkoutsForDayUseCase(day),
                ) { meals, drinks, workouts -> DayEntries(day, meals, drinks, workouts) }
            }

        val uiState: StateFlow<TodayUiState> =
            combine(dayEntries, targets, currentDay) { entries, targets, today ->
                TodayUiState(
                    date = entries.date,
                    maxSelectableDate = today,
                    targets = targets,
                    nutritionEntries = nutritionEntries(entries.meals, entries.drinks),
                    workoutEntries = workoutEntries(entries.workouts),
                    totals = calculateDailyIntakeTotals(entries.meals, entries.drinks),
                    canGoToNextDay = canGoToNextDay(entries.date, today),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = initialState(),
            )

        fun onEvent(event: TodayUiEvent) {
            when (event) {
                TodayUiEvent.OnPreviousDay -> selectedDate.update { it.minusDays(1) }
                TodayUiEvent.OnNextDay -> selectedDate.update { nextDay(it, today()) }
                is TodayUiEvent.OnDateSelected ->
                    selectedDate.update { coerceNotFuture(event.date, today()) }
            }
        }

        fun onScreenResumed() {
            val today = today()
            currentDay.value = today
            selectedDate.update { coerceNotFuture(it, today) }
        }

        private fun initialState(): TodayUiState {
            val today = today()
            return TodayUiState(
                date = today,
                maxSelectableDate = today,
                targets = initialTargets,
            )
        }

        private data class DayEntries(
            val date: LocalDate,
            val meals: List<Meal>,
            val drinks: List<Drink>,
            val workouts: List<Workout>,
        )

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
