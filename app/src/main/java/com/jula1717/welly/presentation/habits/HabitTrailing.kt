package com.jula1717.welly.presentation.habits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitDaySummary
import com.jula1717.welly.presentation.components.PillStepper
import com.jula1717.welly.presentation.util.hasDailyCounter
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun HabitTrailing(
    summary: HabitDaySummary,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val habit = summary.habit
    val status = summary.state.status
    val count = summary.state.count
    val times = habit.frequency.times
    val builtIn = habit.builtIn

    when {
        builtIn != null -> BuiltInHabitStatus(builtIn = builtIn, status = status, modifier = modifier)
        habit.frequency.hasDailyCounter ->
            PillStepper(
                text = "$count/$times",
                onDecrement = { onCountChange(count - 1) },
                onIncrement = { onCountChange(count + 1) },
                decrementDescription = stringResource(R.string.habit_cd_decrease),
                incrementDescription = stringResource(R.string.habit_cd_increase),
                modifier = modifier,
                decrementEnabled = count > 0,
                incrementEnabled = count < times,
                highlighted = status == HabitDayStatus.Done,
                lightButtons = true,
            )
        else ->
            HabitCheckButton(
                status = status,
                modifier = modifier,
                onClick = { onCountChange(if (count > 0) 0 else 1) },
            )
    }
}

@PreviewLightDark
@Composable
private fun HabitTrailingPreview() {
    WellyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PreviewHabitSummaries.forEach { summary ->
                HabitTrailing(summary = summary, onCountChange = {})
            }
        }
    }
}
