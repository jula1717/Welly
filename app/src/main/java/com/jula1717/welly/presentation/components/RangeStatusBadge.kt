package com.jula1717.welly.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun RangeStatusBadge(
    status: RangeStatus,
    modifier: Modifier = Modifier,
) {
    val onTarget = status == RangeStatus.Within

    val containerColor = if (onTarget) {
        MaterialTheme.colorScheme.inversePrimary
    } else {
        MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = if (onTarget) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onErrorContainer
    }

    Box(
        modifier = modifier
            .background(containerColor, CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = status.icon,
            contentDescription = stringResource(status.contentDescriptionRes),
            tint = contentColor,
            modifier = Modifier.size(14.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun RangeStatusBadgePreview() {
    WellyTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
        ) {
            RangeStatusBadge(status = RangeStatus.Below)
            RangeStatusBadge(status = RangeStatus.Within)
            RangeStatusBadge(status = RangeStatus.Above)
        }
    }
}
