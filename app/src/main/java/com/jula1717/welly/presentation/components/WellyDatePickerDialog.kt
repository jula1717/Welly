package com.jula1717.welly.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jula1717.welly.R
import com.jula1717.welly.presentation.util.toUtcEpochMillis
import com.jula1717.welly.presentation.util.toUtcLocalDate
import com.jula1717.welly.ui.theme.WellyTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WellyDatePickerDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toUtcEpochMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onConfirm(millis.toUtcLocalDate())
                    }
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        modifier = modifier,
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
        )
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
