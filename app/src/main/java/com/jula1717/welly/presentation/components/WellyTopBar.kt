package com.jula1717.welly.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun WellyTopBar(
    @StringRes titleRes: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    WellyTopBar(
        title = stringResource(titleRes),
        onBack = onBack,
        modifier = modifier,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WellyTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (subtitle == null) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.close),
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        windowInsets = WindowInsets(0),
    )
}

@PreviewLightDark
@Composable
private fun WellyTopBarPreview() {
    WellyTheme {
        WellyTopBar(titleRes = R.string.add_meal_title, onBack = {})
    }
}

@PreviewLightDark
@Composable
private fun WellyTopBarWithSubtitleAndActionPreview() {
    WellyTheme {
        WellyTopBar(
            title = "Gym",
            subtitle = "2 times a week · 15 pts",
            onBack = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            },
        )
    }
}
