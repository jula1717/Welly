package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.streakUnit
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun StreakTiles(
    currentStreak: Int,
    longestStreak: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StreakTile(
            label = stringResource(R.string.habit_stats_current_streak),
            value = currentStreak,
            unit = streakUnit(currentStreak),
            showFlame = currentStreak > 0,
            modifier = Modifier.weight(1f),
        )
        StreakTile(
            label = stringResource(R.string.habit_stats_longest_streak),
            value = longestStreak,
            unit = streakUnit(longestStreak),
            showFlame = false,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StreakTile(
    label: String,
    value: Int,
    unit: String,
    showFlame: Boolean,
    modifier: Modifier = Modifier,
) {
    StatsCard(
        modifier = modifier,
        cornerRadius = 16.dp,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            if (showFlame) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = unit,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun StreakTilesPreview() {
    WellyTheme {
        StreakTiles(
            currentStreak = 21,
            longestStreak = 48,
            modifier = Modifier.padding(16.dp),
        )
    }
}
