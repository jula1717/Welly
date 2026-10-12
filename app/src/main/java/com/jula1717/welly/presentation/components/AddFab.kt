package com.jula1717.welly.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun AddFab(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = contentDescription,
        )
    }
}

@PreviewLightDark
@Composable
private fun AddFabPreview() {
    WellyTheme {
        AddFab(
            contentDescription = stringResource(R.string.habits_add),
            onClick = {},
        )
    }
}
