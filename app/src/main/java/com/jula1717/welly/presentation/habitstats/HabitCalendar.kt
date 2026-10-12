package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
internal fun HabitCalendar(
    month: YearMonth,
    today: LocalDate,
    days: Map<LocalDate, HabitDayState>,
    canGoToPreviousMonth: Boolean,
    canGoToNextMonth: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weeks = remember(month) { monthWeeks(month) }

    StatsCard(modifier = modifier) {
        MonthHeader(
            month = month,
            canGoToPrevious = canGoToPreviousMonth,
            canGoToNext = canGoToNextMonth,
            onPrevious = onPreviousMonth,
            onNext = onNextMonth,
        )
        WeekdayHeader(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp))

        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                week.forEach { date ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (date != null) {
                            val state = if (date > today) null else days[date] ?: HabitDayState.Empty
                            HabitCalendarDay(dayOfMonth = date.dayOfMonth, state = state)
                        }
                    }
                }
            }
        }

        CalendarLegend(modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun MonthHeader(
    month: YearMonth,
    canGoToPrevious: Boolean,
    canGoToNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    val locale = LocalLocale.current.platformLocale
    val formatter = remember(locale) { DateTimeFormatter.ofPattern("LLLL yyyy", locale) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onPrevious, enabled = canGoToPrevious) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.habit_cd_previous_month),
            )
        }
        Text(
            text = month.format(formatter),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        IconButton(onClick = onNext, enabled = canGoToNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.habit_cd_next_month),
            )
        }
    }
}

@Composable
private fun WeekdayHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        stringArrayResource(R.array.habit_weekdays_short).forEach { weekday ->
            Text(
                text = weekday,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun HabitCalendarPreview() {
    WellyTheme {
        HabitCalendar(
            month = YearMonth.from(PreviewToday),
            today = PreviewToday,
            days = PreviewCalendarDays,
            canGoToPreviousMonth = true,
            canGoToNextMonth = false,
            onPreviousMonth = {},
            onNextMonth = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
