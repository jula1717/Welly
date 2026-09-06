package com.jula1717.welly.presentation.today

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle

@Composable
internal fun DayHeader(
    date: LocalDate,
    canGoToNextDay: Boolean,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateClick: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: (() -> Unit) = {},
    modifier: Modifier = Modifier,
) {
    val locale = LocalLocale.current.platformLocale
    val dateFormatter = remember(locale) {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderActionButton(
            icon = Icons.Outlined.AccountCircle,
            contentDescription = stringResource(R.string.cd_open_profile),
            onClick = onOpenProfile,
        )

        IconButton(onClick = onPreviousDay) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cd_previous_day),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onDateClick),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.FULL, locale),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = date.format(dateFormatter),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        IconButton(onClick = onNextDay, enabled = canGoToNextDay) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.cd_next_day),
                tint = if (canGoToNextDay) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                },
            )
        }

        HeaderActionButton(
            icon = Icons.Outlined.Settings,
            contentDescription = stringResource(R.string.cd_open_settings),
            onClick = onOpenSettings,
        )
    }
}

@Composable
private fun HeaderActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@PreviewLightDark
@Composable
private fun DayHeaderPreview() {
    WellyTheme {
        DayHeader(
            date = LocalDate.now(),
            canGoToNextDay = false,
            onPreviousDay = {},
            onNextDay = {},
            onDateClick = {},
            onOpenProfile = {},
            onOpenSettings = {},
        )
    }
}
