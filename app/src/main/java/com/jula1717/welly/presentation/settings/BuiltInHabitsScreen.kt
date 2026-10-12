package com.jula1717.welly.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.presentation.components.WellyTopBar
import com.jula1717.welly.presentation.util.icon
import com.jula1717.welly.presentation.util.label
import com.jula1717.welly.presentation.util.nameResId
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun BuiltInHabitsDestination(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BuiltInHabitsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BuiltInHabitsScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
internal fun BuiltInHabitsScreen(
    state: BuiltInHabitsUiState,
    onBack: () -> Unit,
    onEvent: (BuiltInHabitsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        WellyTopBar(titleRes = R.string.settings_habits_section, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_habits_intro),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            state.habits.forEach { setting ->
                val toggle = { enabled: Boolean ->
                    onEvent(BuiltInHabitsUiEvent.OnEnabledChanged(setting.id, enabled))
                }

                SettingsRow(
                    icon = setting.type.icon,
                    title = stringResource(setting.type.nameResId),
                    subtitle = setting.frequency.label(),
                    onClick = { toggle(!setting.isEnabled) },
                    trailing = { Switch(checked = setting.isEnabled, onCheckedChange = toggle) },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BuiltInHabitsScreenPreview() {
    WellyTheme {
        BuiltInHabitsScreen(
            state = BuiltInHabitsUiState(
                habits = BuiltInHabit.entries.mapIndexed { index, type ->
                    BuiltInHabitSetting(
                        id = index + 1L,
                        type = type,
                        frequency = HabitFrequency.Daily,
                        isEnabled = index != 1,
                    )
                },
            ),
            onBack = {},
            onEvent = {},
        )
    }
}
