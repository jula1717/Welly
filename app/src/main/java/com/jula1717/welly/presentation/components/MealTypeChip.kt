package com.jula1717.welly.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MealTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            borderWidth = 1.5.dp,
            selectedBorderWidth = 1.5.dp,
        ),
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun MealTypeChipSelectedPreview() {
    WellyTheme {
        MealTypeChip(
            label = "Breakfast",
            selected = true,
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun MealTypeChipUnselectedPreview() {
    WellyTheme {
        MealTypeChip(
            label = "Breakfast",
            selected = false,
            onClick = {},
        )
    }
}
