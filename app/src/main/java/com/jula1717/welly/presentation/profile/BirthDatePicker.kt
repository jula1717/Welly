package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate

// Month names are the widest labels; day is at most two digits, year always four.
private const val DAY_COLUMN_WEIGHT = 1f
private const val MONTH_COLUMN_WEIGHT = 2.1f
private const val YEAR_COLUMN_WEIGHT = 1.4f

private val FIELD_SHAPE = RoundedCornerShape(18.dp)

@Composable
internal fun BirthDatePicker(
    dateOfBirth: LocalDate,
    maxDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = LocalLocale.current.platformLocale
    // cheap enough to run on every recomposition
    val current = clampBirthDate(dateOfBirth.year, dateOfBirth.monthValue, dateOfBirth.dayOfMonth, maxDate)

    fun updateDate(
        year: Int,
        month: Int,
        day: Int,
    ) = onDateChange(clampBirthDate(year, month, day, maxDate))

    val yearOptions = remember(maxDate) { birthYearRange(maxDate).toList() }
    val monthOptions =
        remember(current.year, maxDate) { (1..maxBirthMonth(current.year, maxDate)).toList() }
    val dayOptions =
        remember(current.year, current.monthValue, maxDate) {
            (1..maxBirthDay(current.year, current.monthValue, maxDate)).toList()
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DateDropdown(
            label = stringResource(R.string.profile_dob_day),
            selected = current.dayOfMonth,
            options = dayOptions,
            optionLabel = Int::toString,
            onSelected = { updateDate(current.year, current.monthValue, it) },
            modifier = Modifier.weight(DAY_COLUMN_WEIGHT),
        )
        DateDropdown(
            label = stringResource(R.string.profile_dob_month),
            selected = current.monthValue,
            options = monthOptions,
            optionLabel = { birthMonthName(it, locale) },
            onSelected = { updateDate(current.year, it, current.dayOfMonth) },
            modifier = Modifier.weight(MONTH_COLUMN_WEIGHT),
        )
        DateDropdown(
            label = stringResource(R.string.profile_dob_year),
            selected = current.year,
            options = yearOptions,
            optionLabel = Int::toString,
            onSelected = { updateDate(it, current.monthValue, current.dayOfMonth) },
            modifier = Modifier.weight(YEAR_COLUMN_WEIGHT),
        )
    }
}

@Composable
private fun DateDropdown(
    label: String,
    selected: Int,
    options: List<Int>,
    optionLabel: (Int) -> String,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val itemHeightPx = with(LocalDensity.current) { 48.dp.roundToPx() }
    val valueText = optionLabel(selected)
    val fieldDescription = stringResource(R.string.cd_labeled_field, label, valueText)

    LaunchedEffect(expanded) {
        if (expanded) {
            scrollState.scrollTo(options.indexOf(selected) * itemHeightPx)
        }
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 3.dp),
        )
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(FIELD_SHAPE)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, FIELD_SHAPE)
                    .clickable { expanded = true }
                    .semantics { contentDescription = fieldDescription }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                scrollState = scrollState,
                modifier = Modifier.heightIn(max = 280.dp),
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option)) },
                        onClick = {
                            expanded = false
                            if (option != selected) onSelected(option)
                        },
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BirthDatePickerPreview() {
    WellyTheme {
        val sampleMaxDate = LocalDate.of(2026, 9, 5)
        BirthDatePicker(
            dateOfBirth = sampleMaxDate.minusYears(30L),
            maxDate = sampleMaxDate,
            onDateChange = {},
        )
    }
}
