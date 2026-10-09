package com.jula1717.welly.presentation.addworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.presentation.components.TypeChip
import com.jula1717.welly.presentation.util.titleResId
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ActivityTypeFilter(
    selected: ActivityType?,
    onSelected: (ActivityType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TypeChip(
            label = stringResource(R.string.activity_type_all),
            selected = selected == null,
            onClick = { onSelected(null) },
        )
        ActivityType.entries.forEach { type ->
            TypeChip(
                label = stringResource(type.titleResId),
                selected = selected == type,
                onClick = { onSelected(type) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ActivityTypeFilterPreview() {
    WellyTheme {
        ActivityTypeFilter(selected = ActivityType.Cardio, onSelected = {})
    }
}
