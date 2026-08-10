package com.jula1717.welly.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun MacroChip(
    label: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.clearAndSetSemantics {
            this.contentDescription = contentDescription
        },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun MacroChipPreview() {
    WellyTheme {
        MacroChip(
            label = "P 30",
            contentDescription = "30 grams of protein",
        )
    }
}
