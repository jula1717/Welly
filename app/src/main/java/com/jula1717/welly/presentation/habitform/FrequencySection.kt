package com.jula1717.welly.presentation.habitform

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun FrequencySection(
    input: FrequencyInput,
    onEvent: (HabitFormUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FrequencyOption.entries.forEach { option ->
            FrequencyOptionRow(
                option = option,
                input = input,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun FrequencyOptionRow(
    option: FrequencyOption,
    input: FrequencyInput,
    onEvent: (HabitFormUiEvent) -> Unit,
) {
    val selected = input.option == option
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp)
            .clip(shape)
            .background(if (selected) colors.primary else colors.surfaceContainerLowest)
            .border(1.dp, if (selected) colors.primary else colors.outlineVariant, shape)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = { onEvent(HabitFormUiEvent.OnFrequencyOptionSelected(option)) },
            ).padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FrequencyRowContent(option = option, input = input, selected = selected, onEvent = onEvent)
    }
}

@Composable
private fun RowScope.FrequencyRowContent(
    option: FrequencyOption,
    input: FrequencyInput,
    selected: Boolean,
    onEvent: (HabitFormUiEvent) -> Unit,
) {
    val color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val numberBox: @Composable (FrequencyField) -> Unit = { field ->
        FrequencyNumberBox(
            value = input.valueOf(field),
            onValueChange = { onEvent(HabitFormUiEvent.OnFrequencyValueChanged(field, it)) },
            onFocus = { onEvent(HabitFormUiEvent.OnFrequencyOptionSelected(option)) },
            rowSelected = selected,
        )
    }

    when (option) {
        FrequencyOption.TimesPerDay -> {
            numberBox(FrequencyField.TimesPerDay)
            RowText(R.string.habit_form_times_suffix_per_day, selected, color)
        }
        FrequencyOption.Daily -> RowText(R.string.habit_frequency_daily, selected, color)
        FrequencyOption.EveryNDays -> {
            RowText(R.string.habit_form_every, selected, color)
            numberBox(FrequencyField.EveryDays)
            RowText(R.string.habit_form_days, selected, color)
        }
        FrequencyOption.TimesPerWeek -> {
            numberBox(FrequencyField.TimesPerWeek)
            RowText(R.string.habit_form_times_suffix_per_week, selected, color)
        }
        FrequencyOption.TimesPerMonth -> {
            numberBox(FrequencyField.TimesPerMonth)
            RowText(R.string.habit_form_times_suffix_per_month, selected, color)
        }
        FrequencyOption.TimesPerNDays -> {
            numberBox(FrequencyField.TimesInDays)
            RowText(R.string.habit_form_times_per, selected, color)
            numberBox(FrequencyField.DaysWindow)
            RowText(R.string.habit_form_days, selected, color)
        }
    }
}

@Composable
private fun RowText(
    @StringRes textRes: Int,
    selected: Boolean,
    color: Color,
) {
    Text(
        text = stringResource(textRes),
        fontSize = 14.5.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
        color = color,
    )
}

private val PREVIEW_FREQUENCY = HabitFrequency(times = 10, periodLength = 14, periodUnit = HabitPeriodUnit.Day)

@PreviewLightDark
@Composable
private fun FrequencySectionPreview() {
    WellyTheme {
        FrequencySection(
            input = FrequencyInput.from(PREVIEW_FREQUENCY),
            onEvent = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun FrequencySectionDailyPreview() {
    WellyTheme {
        FrequencySection(
            input = FrequencyInput(option = FrequencyOption.Daily),
            onEvent = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
