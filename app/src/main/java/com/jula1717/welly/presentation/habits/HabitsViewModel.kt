package com.jula1717.welly.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.EnsureBuiltInHabitsUseCase
import com.jula1717.welly.domain.usecase.GetHabitSummariesUseCase
import com.jula1717.welly.domain.usecase.SetHabitCompletionCountUseCase
import com.jula1717.welly.presentation.util.canGoToNextDay
import com.jula1717.welly.presentation.util.coerceNotFuture
import com.jula1717.welly.presentation.util.nextDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HabitsViewModel
    @Inject
    constructor(
        getHabitSummaries: GetHabitSummariesUseCase,
        ensureBuiltInHabits: EnsureBuiltInHabitsUseCase,
        private val setHabitCompletionCount: SetHabitCompletionCountUseCase,
        private val clock: Clock,
    ) : ViewModel() {
        private fun today(): LocalDate = LocalDate.now(clock)

        /** The day the user is currently looking at. */
        private val selectedDate = MutableStateFlow(today())

        /** "Today", re-read on every screen resume so a midnight rollover is picked up. */
        private val currentDay = MutableStateFlow(today())

        private val summaries =
            selectedDate.flatMapLatest { date ->
                getHabitSummaries(date).map { habits -> date to habits }
            }

        val uiState: StateFlow<HabitsUiState> =
            combine(summaries, currentDay) { (date, habits), today ->
                HabitsUiState(
                    date = date,
                    maxSelectableDate = today,
                    canGoToNextDay = canGoToNextDay(date, today),
                    habits = habits,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = initialState(),
            )

        init {
            viewModelScope.launch { ensureBuiltInHabits() }
        }

        fun onEvent(event: HabitsUiEvent) {
            when (event) {
                HabitsUiEvent.OnPreviousDay -> selectedDate.update { it.minusDays(1) }
                HabitsUiEvent.OnNextDay -> selectedDate.update { nextDay(it, today()) }
                is HabitsUiEvent.OnDateSelected ->
                    selectedDate.update { coerceNotFuture(event.date, today()) }
                is HabitsUiEvent.OnCountChanged -> setCount(event.habitId, event.count)
            }
        }

        fun onScreenResumed() {
            val today = today()
            currentDay.value = today
            selectedDate.update { coerceNotFuture(it, today) }
        }

        private fun setCount(
            habitId: Long,
            count: Int,
        ) {
            val date = selectedDate.value
            viewModelScope.launch { setHabitCompletionCount(habitId, date, count) }
        }

        private fun initialState(): HabitsUiState {
            val today = today()
            return HabitsUiState(date = today, maxSelectableDate = today)
        }

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
