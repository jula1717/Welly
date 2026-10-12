package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitStats
import com.jula1717.welly.presentation.components.CircleIconButton
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.presentation.util.displayName
import com.jula1717.welly.presentation.util.label
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.YearMonth

@Composable
fun HabitStatsDestination(
    onBack: () -> Unit,
    onEditHabit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitStatsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HabitStatsEffect.NavigateBack -> onBack()
            }
        }
    }

    HabitStatsScreen(
        state = state,
        onBack = onBack,
        onEditHabit = onEditHabit,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun HabitStatsScreen(
    state: HabitStatsUiState,
    onBack: () -> Unit,
    onEditHabit: (Long) -> Unit,
    onEvent: (HabitStatsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val habit = state.habit

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (habit != null) {
            HabitStatsTopBar(habit = habit, onBack = onBack, onEdit = { onEditHabit(habit.id) })

            state.stats?.let { stats ->
                HabitStatsContent(
                    stats = stats,
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun HabitStatsTopBar(
    habit: Habit,
    onBack: () -> Unit,
    onEdit: () -> Unit,
) {
    WellyTopBar(
        title = habit.displayName(),
        subtitle = stringResource(
            R.string.habit_stats_subtitle,
            habit.frequency.label(),
            stringResource(R.string.habit_points_short, habit.points),
        ),
        onBack = onBack,
        actions = {
            CircleIconButton(
                icon = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.habit_stats_edit),
                onClick = onEdit,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(end = 8.dp),
            )
        },
    )
}

@Composable
private fun HabitStatsContent(
    stats: HabitStats,
    state: HabitStatsUiState,
    onEvent: (HabitStatsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        StreakTiles(
            currentStreak = stats.currentStreak,
            longestStreak = stats.longestStreak,
        )
        RateTiles(last7Days = stats.rateLast7Days, last30Days = stats.rateLast30Days)
        HabitCalendar(
            month = state.visibleMonth,
            today = state.today,
            days = stats.days,
            canGoToPreviousMonth = state.canGoToPreviousMonth,
            canGoToNextMonth = state.canGoToNextMonth,
            onPreviousMonth = { onEvent(HabitStatsUiEvent.OnPreviousMonth) },
            onNextMonth = { onEvent(HabitStatsUiEvent.OnNextMonth) },
        )
        MonthlyCountChart(counts = stats.monthlyCounts)
    }
}

@PreviewLightDark
@Composable
private fun HabitStatsScreenPreview() {
    WellyTheme {
        HabitStatsScreen(
            state = HabitStatsUiState(
                today = PreviewToday,
                habit = PreviewHabit,
                stats = PreviewStats,
                earliestMonth = YearMonth.from(PreviewToday).minusMonths(2),
            ),
            onBack = {},
            onEditHabit = {},
            onEvent = {},
        )
    }
}
