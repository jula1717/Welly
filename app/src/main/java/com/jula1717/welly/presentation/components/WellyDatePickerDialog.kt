package com.jula1717.welly.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.toUtcEpochMillis
import com.jula1717.welly.presentation.util.toUtcLocalDate
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WellyDatePickerDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    maxDate: LocalDate = LocalDate.now(),
) {
    val selectableDates = remember(maxDate) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= maxDate.toUtcEpochMillis()

            override fun isSelectableYear(year: Int): Boolean = year <= maxDate.year
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toUtcEpochMillis(),
        selectableDates = selectableDates,
    )
    val dialogWidth = LocalConfiguration.current.screenWidthDp.dp * 0.8f

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.wrapContentHeight(),
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.width(dialogWidth),
            shape = DatePickerDefaults.shape,
            tonalElevation = DatePickerDefaults.TonalElevation,
            color = DatePickerDefaults.colors().containerColor,
        ) {
            Column {
                Box(Modifier.weight(1f, fill = false)) {
                    ScaledToFitWidth {
                        DatePicker(
                            modifier = Modifier.requiredWidth(360.dp),
                            state = datePickerState,
                            showModeToggle = false,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, end = 6.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onConfirm(millis.toUtcLocalDate())
                            }
                        },
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScaledToFitWidth(content: @Composable () -> Unit) {
    BoxWithConstraints {
        val scale = (maxWidth / 360.dp).coerceAtMost(1f)
        Layout(content = content) { measurables, _ ->
            val placeable = measurables.first().measure(Constraints())
            val width = (placeable.width * scale).roundToInt()
            val height = (placeable.height * scale).roundToInt()
            layout(width, height) {
                placeable.placeWithLayer(IntOffset.Zero) {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun WellyDatePickerDialogPreview() {
    WellyTheme {
        WellyDatePickerDialog(
            initialDate = LocalDate.now(),
            onDismiss = {},
            onConfirm = {},
        )
    }
}
