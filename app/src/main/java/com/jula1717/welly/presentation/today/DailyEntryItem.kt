package com.jula1717.welly.presentation.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.presentation.util.TIME_DISPLAY_FORMAT
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalTime

@Composable
internal fun DailyEntryItem(
    entry: TodayEntry,
    modifier: Modifier = Modifier,
) {
    val display = entry.toEntryDisplay()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = entry.time.format(TIME_DISPLAY_FORMAT),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(40.dp),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = display.typeLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = display.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = stringResource(R.string.kcal_amount, entry.calories),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun DailyEntryItemMealPreview() {
    WellyTheme {
        DailyEntryItem(
            entry = TodayEntry.MealEntry(
                id = 1,
                time = LocalTime.of(8, 42),
                type = MealType.Breakfast,
                description = "Oatmeal with banana and peanut butter",
                calories = 486,
            ),
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun DailyEntryItemDrinkPreview() {
    WellyTheme {
        DailyEntryItem(
            entry = TodayEntry.DrinkEntry(
                id = 1,
                time = LocalTime.of(18, 10),
                amountMl = 330,
                description = "Latte with 2% milk",
                calories = 168,
            ),
        )
    }
}
