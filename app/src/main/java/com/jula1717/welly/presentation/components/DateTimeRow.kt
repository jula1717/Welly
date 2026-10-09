package com.jula1717.welly.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.DATE_DISPLAY_FORMAT
import com.jula1717.welly.presentation.util.TIME_DISPLAY_FORMAT
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun DateTimeRow(
    date: LocalDate,
    time: LocalTime,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DateTimeCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.date_label),
            value = date.format(DATE_DISPLAY_FORMAT),
            icon = Icons.Default.CalendarToday,
            onClick = onDateClick,
        )
        DateTimeCard(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.time_label),
            value = time.format(TIME_DISPLAY_FORMAT),
            icon = Icons.Default.Schedule,
            onClick = onTimeClick,
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun DateTimeRowPreview() {
    WellyTheme {
        DateTimeRow(
            date = LocalDate.of(2026, 10, 9),
            time = LocalTime.of(14, 30),
            onDateClick = {},
            onTimeClick = {},
        )
    }
}
