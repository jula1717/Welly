package com.jula1717.welly.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun MacroChipItem(
    type: MacroType,
    value: Int,
    modifier: Modifier = Modifier,
) {
    MacroChip(
        label = stringResource(type.titleResId, value),
        contentDescription = stringResource(type.contentDescriptionResId, value),
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun MacroChipItemPreview() {
    WellyTheme {
        MacroChipItem(type = MacroType.PROTEIN, value = 30)
    }
}
