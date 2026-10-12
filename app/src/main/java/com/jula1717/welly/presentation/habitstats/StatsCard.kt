package com.jula1717.welly.presentation.habitstats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun StatsCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(contentPadding),
        content = content,
    )
}

@PreviewLightDark
@Composable
private fun StatsCardPreview() {
    WellyTheme {
        StatsCard(modifier = Modifier.padding(16.dp)) {
            Text(text = "Card content", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
