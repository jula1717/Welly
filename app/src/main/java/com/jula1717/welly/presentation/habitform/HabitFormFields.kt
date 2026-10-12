package com.jula1717.welly.presentation.habitform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.components.PillStepper
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun HabitNameField(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
    )
}

@Composable
internal fun HabitPointsField(
    points: Int,
    onPointsChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PillStepper(
        text = stringResource(R.string.habit_points_short, points),
        onDecrement = { onPointsChange(points - 1) },
        onIncrement = { onPointsChange(points + 1) },
        decrementDescription = stringResource(R.string.habit_cd_decrease),
        incrementDescription = stringResource(R.string.habit_cd_increase),
        modifier = modifier,
        decrementEnabled = points > 0,
        incrementEnabled = points < HabitFormUiState.MAX_POINTS,
        minTextWidth = 64.dp,
    )
}

@PreviewLightDark
@Composable
private fun HabitFormFieldsPreview() {
    WellyTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            HabitNameField(name = "Brushing teeth", onNameChange = {})
            HabitPointsField(points = 10, onPointsChange = {}, modifier = Modifier.padding(top = 12.dp))
        }
    }
}
