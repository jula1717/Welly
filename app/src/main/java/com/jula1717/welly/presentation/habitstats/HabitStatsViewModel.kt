package com.jula1717.welly.presentation.habitstats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.GetHabitStatsUseCase
import com.jula1717.welly.domain.usecase.GetHabitUseCase
import com.jula1717.welly.presentation.navigation.HabitStatsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HabitStatsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getHabit: GetHabitUseCase,
        getHabitStats: GetHabitStatsUseCase,
        clock: Clock,
    ) : ViewModel() {
        private val habitId: Long = checkNotNull(savedStateHandle[HabitStatsDestination.HABIT_ID_KEY])
        private val today = LocalDate.now(clock)
        private val visibleMonth = MutableStateFlow(YearMonth.from(today))

        val uiState: StateFlow<HabitStatsUiState> =
            combine(getHabit(habitId), getHabitStats(habitId), visibleMonth) { habit, stats, month ->
                HabitStatsUiState(
                    today = today,
                    visibleMonth = month,
                    habit = habit,
                    stats = stats,
                    earliestMonth = YearMonth.from(stats?.firstCompletion ?: today),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = HabitStatsUiState(today = today),
            )

        private val _effects = Channel<HabitStatsEffect>()
        val effects: Flow<HabitStatsEffect> = _effects.receiveAsFlow()

        init {
            viewModelScope.launch {
                getHabit(habitId).collect { habit ->
                    if (habit == null) _effects.send(HabitStatsEffect.NavigateBack)
                }
            }
        }

        fun onEvent(event: HabitStatsUiEvent) {
            when (event) {
                HabitStatsUiEvent.OnPreviousMonth ->
                    if (uiState.value.canGoToPreviousMonth) visibleMonth.update { it.minusMonths(1) }
                HabitStatsUiEvent.OnNextMonth ->
                    if (uiState.value.canGoToNextMonth) visibleMonth.update { it.plusMonths(1) }
            }
        }

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
