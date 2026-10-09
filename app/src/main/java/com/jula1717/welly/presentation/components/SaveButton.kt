package com.jula1717.welly.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        text = stringResource(R.string.save),
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun SaveButtonPreview() {
    WellyTheme {
        SaveButton(enabled = true, onClick = {})
    }
}

@PreviewLightDark
@Composable
private fun SaveButtonDisabledPreview() {
    WellyTheme {
        SaveButton(enabled = false, onClick = {})
    }
}
