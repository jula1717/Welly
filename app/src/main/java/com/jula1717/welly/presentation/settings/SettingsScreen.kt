package com.jula1717.welly.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun SettingsDestination(
    onBack: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenActivities: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onBack = onBack,
        onOpenProfile = onOpenProfile,
        onOpenActivities = onOpenActivities,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenActivities: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.settings_title, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingsRow(
                icon = Icons.Outlined.Person,
                title = stringResource(R.string.settings_profile_title),
                subtitle = stringResource(R.string.settings_profile_subtitle),
                onClick = onOpenProfile,
            )

            SettingsRow(
                icon = Icons.Outlined.FitnessCenter,
                title = stringResource(R.string.settings_activities_title),
                subtitle = pluralStringResource(
                    R.plurals.settings_activities_count,
                    state.activityCount,
                    state.activityCount,
                ),
                onClick = onOpenActivities,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    WellyTheme {
        SettingsScreen(
            state = SettingsUiState(activityCount = 8),
            onBack = {},
            onOpenProfile = {},
            onOpenActivities = {},
        )
    }
}
