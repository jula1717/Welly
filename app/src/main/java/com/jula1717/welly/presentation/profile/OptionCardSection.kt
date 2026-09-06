package com.jula1717.welly.presentation.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun ActivitySection(
    selected: ActivityLevel?,
    onSelected: (ActivityLevel) -> Unit,
) {
    OptionCardSection(
        label = stringResource(R.string.profile_activity_label),
        options = ActivityLevel.entries,
        selected = selected,
        titleRes = ActivityLevel::titleRes,
        descriptionRes = ActivityLevel::descriptionRes,
        onSelected = onSelected,
    )
}

@Composable
internal fun GoalSection(
    selected: NutritionGoal?,
    onSelected: (NutritionGoal) -> Unit,
) {
    OptionCardSection(
        label = stringResource(R.string.profile_goal_label),
        options = NutritionGoal.entries,
        selected = selected,
        titleRes = NutritionGoal::titleRes,
        descriptionRes = NutritionGoal::descriptionRes,
        onSelected = onSelected,
    )
}

@Composable
private fun <T> OptionCardSection(
    label: String,
    options: List<T>,
    selected: T?,
    titleRes: (T) -> Int,
    descriptionRes: (T) -> Int,
    onSelected: (T) -> Unit,
) {
    LabeledSection(label) {
        Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            options.forEach { option ->
                SelectableOptionCard(
                    title = stringResource(titleRes(option)),
                    description = stringResource(descriptionRes(option)),
                    selected = option == selected,
                    onClick = { onSelected(option) },
                )
            }
        }
    }
}

@StringRes
private fun ActivityLevel.titleRes(): Int =
    when (this) {
        ActivityLevel.Sedentary -> R.string.profile_activity_sedentary_title
        ActivityLevel.Light -> R.string.profile_activity_light_title
        ActivityLevel.Moderate -> R.string.profile_activity_moderate_title
        ActivityLevel.Active -> R.string.profile_activity_active_title
        ActivityLevel.VeryActive -> R.string.profile_activity_very_active_title
    }

@StringRes
private fun ActivityLevel.descriptionRes(): Int =
    when (this) {
        ActivityLevel.Sedentary -> R.string.profile_activity_sedentary_desc
        ActivityLevel.Light -> R.string.profile_activity_light_desc
        ActivityLevel.Moderate -> R.string.profile_activity_moderate_desc
        ActivityLevel.Active -> R.string.profile_activity_active_desc
        ActivityLevel.VeryActive -> R.string.profile_activity_very_active_desc
    }

@StringRes
private fun NutritionGoal.titleRes(): Int =
    when (this) {
        NutritionGoal.Lose -> R.string.profile_goal_lose_title
        NutritionGoal.Maintain -> R.string.profile_goal_maintain_title
        NutritionGoal.Gain -> R.string.profile_goal_gain_title
    }

@StringRes
private fun NutritionGoal.descriptionRes(): Int =
    when (this) {
        NutritionGoal.Lose -> R.string.profile_goal_lose_desc
        NutritionGoal.Maintain -> R.string.profile_goal_maintain_desc
        NutritionGoal.Gain -> R.string.profile_goal_gain_desc
    }

@PreviewLightDark
@Composable
private fun OptionCardSectionPreview() {
    WellyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ActivitySection(selected = ActivityLevel.Moderate, onSelected = {})
            GoalSection(selected = NutritionGoal.Maintain, onSelected = {})
        }
    }
}
