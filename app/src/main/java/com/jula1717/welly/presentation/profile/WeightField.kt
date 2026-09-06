package com.jula1717.welly.presentation.profile

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.ClearFocusOnKeyboardHidden
import com.jula1717.welly.ui.theme.WellyTheme
import kotlin.math.roundToInt

private const val WEIGHT_PLACEHOLDER = "0.0"
private val FIELD_SHAPE = RoundedCornerShape(18.dp)

@Composable
internal fun WeightField(
    weightKg: Double?,
    range: ClosedFloatingPointRange<Double>,
    unit: String,
    onWeightChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    var text by rememberSaveable { mutableStateOf(weightKg.toFieldText()) }
    var touched by rememberSaveable { mutableStateOf(false) }

    ClearFocusOnKeyboardHidden(focused)

    LaunchedEffect(weightKg) {
        if (!focused && parseWeight(text) != weightKg) text = weightKg.toFieldText()
    }

    LaunchedEffect(focused) {
        if (focused) touched = true
    }

    val parsed = parseWeight(text)

    val showError = (parsed == null || parsed !in range) && !focused && touched

    OutlinedTextField(
        value = text,
        onValueChange = { raw ->
            val cleaned = sanitizeWeightInput(raw)
            text = cleaned
            onWeightChange(cleaned)
        },
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(WEIGHT_PLACEHOLDER) },
        suffix = { Text(unit) },
        singleLine = true,
        isError = showError,
        supportingText = if (showError) ({ WeightRangeError(range) }) else null,
        shape = FIELD_SHAPE,
        colors = weightFieldColors(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        interactionSource = interactionSource,
    )
}

@Composable
private fun WeightRangeError(range: ClosedFloatingPointRange<Double>) {
    Text(
        stringResource(
            R.string.profile_weight_invalid,
            range.start.roundToInt(),
            range.endInclusive.roundToInt(),
        ),
    )
}

private const val PLACEHOLDER_ALPHA = 0.5f

@Composable
private fun weightFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = PLACEHOLDER_ALPHA),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = PLACEHOLDER_ALPHA),
        errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = PLACEHOLDER_ALPHA),
    )

private fun Double?.toFieldText(): String = this?.let { formatWeight(it).removeSuffix(".0") }.orEmpty()

@PreviewLightDark
@Composable
private fun WeightFieldPreview() {
    WellyTheme {
        WeightField(
            weightKg = 72.5,
            range = ProfileUiState.MIN_WEIGHT_KG..ProfileUiState.MAX_WEIGHT_KG,
            unit = stringResource(R.string.profile_weight_unit),
            onWeightChange = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun WeightFieldEmptyPreview() {
    WellyTheme {
        WeightField(
            weightKg = null,
            range = ProfileUiState.MIN_WEIGHT_KG..ProfileUiState.MAX_WEIGHT_KG,
            unit = stringResource(R.string.profile_weight_unit),
            onWeightChange = {},
        )
    }
}
