package com.jula1717.welly.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun PillStepper(
    text: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    decrementDescription: String,
    incrementDescription: String,
    modifier: Modifier = Modifier,
    decrementEnabled: Boolean = true,
    incrementEnabled: Boolean = true,
    highlighted: Boolean = false,
    lightButtons: Boolean = highlighted,
    minTextWidth: Dp = 40.dp,
) {
    val colors = MaterialTheme.colorScheme
    val containerColor = if (highlighted) colors.primaryContainer else colors.surfaceContainerHigh
    val borderColor = if (highlighted) colors.primaryContainer else colors.outlineVariant
    val textColor = if (highlighted) colors.onPrimaryContainer else colors.onSurface

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .border(1.dp, borderColor, CircleShape)
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        StepperButton(
            icon = Icons.Default.Remove,
            contentDescription = decrementDescription,
            enabled = decrementEnabled,
            onClick = onDecrement,
            light = lightButtons,
        )

        Text(
            text = text,
            modifier = Modifier.defaultMinSize(minWidth = minTextWidth),
            textAlign = TextAlign.Center,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            maxLines = 1,
        )

        StepperButton(
            icon = Icons.Default.Add,
            contentDescription = incrementDescription,
            enabled = incrementEnabled,
            onClick = onIncrement,
            light = lightButtons,
        )
    }
}

@PreviewLightDark
@Composable
private fun PillStepperPreview() {
    WellyTheme {
        PillStepper(
            text = "1/2",
            onDecrement = {},
            onIncrement = {},
            decrementDescription = "Decrease",
            incrementDescription = "Increase",
            modifier = Modifier.padding(8.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun PillStepperHighlightedPreview() {
    WellyTheme {
        PillStepper(
            text = "3/3",
            onDecrement = {},
            onIncrement = {},
            decrementDescription = "Decrease",
            incrementDescription = "Increase",
            incrementEnabled = false,
            highlighted = true,
            modifier = Modifier.padding(8.dp),
        )
    }
}
