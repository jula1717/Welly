package com.jula1717.welly.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun StepperButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    light: Boolean = false,
) {
    val colors = MaterialTheme.colorScheme
    val buttonColors =
        if (light) {
            IconButtonDefaults.filledIconButtonColors(
                containerColor = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                disabledContainerColor = colors.secondaryContainer.copy(alpha = DISABLED_ALPHA),
                disabledContentColor = colors.onSecondaryContainer.copy(alpha = DISABLED_ALPHA),
            )
        } else {
            IconButtonDefaults.filledIconButtonColors(
                containerColor = colors.primaryContainer,
                contentColor = colors.onPrimaryContainer,
                disabledContainerColor = colors.surfaceContainerHighest,
                disabledContentColor = colors.onSurfaceVariant.copy(alpha = DISABLED_ALPHA),
            )
        }

    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(30.dp),
        colors = buttonColors,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
        )
    }
}

private const val DISABLED_ALPHA = 0.5f

@PreviewLightDark
@Composable
private fun StepperButtonPreview() {
    WellyTheme {
        Row(modifier = Modifier.padding(8.dp)) {
            listOf(false, true).forEach { light ->
                StepperButton(Icons.Default.Remove, "", enabled = true, onClick = {}, light = light)
                StepperButton(Icons.Default.Add, "", enabled = false, onClick = {}, light = light)
            }
        }
    }
}
