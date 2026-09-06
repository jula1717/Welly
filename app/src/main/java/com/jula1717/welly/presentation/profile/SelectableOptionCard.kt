package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun SelectableOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val container = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val titleColor = if (selected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val descriptionColor = if (selected) {
        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.75f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(container)
            .border(1.dp, borderColor, shape)
            .selectable(
                selected = selected,
                interactionSource = null,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            ).padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = titleColor,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = descriptionColor,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SelectableOptionCardPreview() {
    WellyTheme {
        Column {
            SelectableOptionCard(
                title = "Moderate",
                description = "Regular training, and you are on your feet most days",
                selected = true,
                onClick = {},
            )
            SelectableOptionCard(
                title = "Active",
                description = "Training almost every day, or a physical job",
                selected = false,
                onClick = {},
            )
        }
    }
}
