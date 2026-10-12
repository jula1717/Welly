package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.presentation.components.SectionLabelWithInfo
import com.jula1717.welly.presentation.components.StepperButton
import com.jula1717.welly.ui.theme.WellyTheme

private val FIELD_SHAPE = RoundedCornerShape(18.dp)

@Composable
internal fun WorkoutsPerWeekField(
    workoutsPerWeek: Int,
    onWorkoutsPerWeekChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(7.dp)) {
        SectionLabelWithInfo(
            label = stringResource(R.string.profile_workouts_label),
            infoDescription = stringResource(R.string.cd_workouts_info),
            infoTitle = stringResource(R.string.profile_workouts_info_title),
            infoBody = stringResource(R.string.profile_workouts_info_body),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(FIELD_SHAPE)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, FIELD_SHAPE)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StepperButton(
                icon = Icons.Default.Remove,
                contentDescription = stringResource(R.string.habit_cd_decrease),
                enabled = workoutsPerWeek > UserProfile.MIN_WORKOUTS_PER_WEEK,
                onClick = { onWorkoutsPerWeekChange(workoutsPerWeek - 1) },
                light = true,
            )
            Text(
                text = pluralStringResource(R.plurals.profile_workouts_value, workoutsPerWeek, workoutsPerWeek),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
            StepperButton(
                icon = Icons.Default.Add,
                contentDescription = stringResource(R.string.habit_cd_increase),
                enabled = workoutsPerWeek < UserProfile.MAX_WORKOUTS_PER_WEEK,
                onClick = { onWorkoutsPerWeekChange(workoutsPerWeek + 1) },
                light = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WorkoutsPerWeekFieldPreview() {
    WellyTheme {
        WorkoutsPerWeekField(
            workoutsPerWeek = 3,
            onWorkoutsPerWeekChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
