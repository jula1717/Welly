package com.jula1717.welly.presentation.today

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.titleResId

internal data class EntryDisplay(
    val typeLabel: String,
    val description: String,
)

@Composable
internal fun TodayEntry.toEntryDisplay(): EntryDisplay =
    when (this) {
        is TodayEntry.MealEntry -> EntryDisplay(
            typeLabel = stringResource(type.titleResId),
            description = description,
        )

        is TodayEntry.DrinkEntry -> EntryDisplay(
            typeLabel = stringResource(R.string.today_add_drink),
            description = drinkDescription(this),
        )
    }

@Composable
private fun drinkDescription(entry: TodayEntry.DrinkEntry): String =
    if (entry.description.isBlank()) {
        stringResource(R.string.today_drink_amount_label, entry.amountMl)
    } else {
        stringResource(
            R.string.today_drink_description_with_amount,
            entry.description,
            entry.amountMl,
        )
    }
