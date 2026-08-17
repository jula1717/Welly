package com.jula1717.welly.presentation.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun AddEntryFab(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        AddEntryOptions(
            visible = expanded,
            onAddMeal = {
                onExpandedChange(false)
                onAddMeal()
            },
            onAddDrink = {
                onExpandedChange(false)
                onAddDrink()
            },
        )

        val fabRotation by animateFloatAsState(
            targetValue = if (expanded) 45f else 0f,
            label = "fabIconRotation",
        )
        val fabContainerColor by animateColorAsState(
            targetValue = if (expanded) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
            label = "fabContainerColor",
        )
        val fabContentColor by animateColorAsState(
            targetValue = if (expanded) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            },
            label = "fabContentColor",
        )

        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) },
            shape = CircleShape,
            containerColor = fabContainerColor,
            contentColor = fabContentColor,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(
                    if (expanded) R.string.close else R.string.today_add,
                ),
                modifier = Modifier.rotate(fabRotation),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddEntryFabCollapsedPreview() {
    WellyTheme {
        AddEntryFab(
            expanded = false,
            onExpandedChange = {},
            onAddMeal = {},
            onAddDrink = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun AddEntryFabExpandedPreview() {
    WellyTheme {
        AddEntryFab(
            expanded = true,
            onExpandedChange = {},
            onAddMeal = {},
            onAddDrink = {},
        )
    }
}
