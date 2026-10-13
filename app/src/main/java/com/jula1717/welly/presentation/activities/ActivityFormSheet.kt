package com.jula1717.welly.presentation.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.presentation.components.CircleIconButton
import com.jula1717.welly.presentation.components.SaveButton
import com.jula1717.welly.presentation.components.SegmentedToggle
import com.jula1717.welly.presentation.util.titleResId
import com.jula1717.welly.ui.theme.WellyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ActivityFormSheet(
    form: ActivityFormState,
    onEvent: (ActivitiesUiEvent) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(ActivitiesUiEvent.OnFormDismissed) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        ActivityFormContent(form = form, onEvent = onEvent)
    }
}

@Composable
private fun ActivityFormContent(
    form: ActivityFormState,
    onEvent: (ActivitiesUiEvent) -> Unit,
) {
    val types = ActivityType.entries

    Column {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(if (form.isNew) R.string.activities_add else R.string.activities_edit),
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (!form.isNew) {
                    CircleIconButton(
                        icon = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.activities_cd_delete),
                        onClick = { onEvent(ActivitiesUiEvent.OnDeleteClicked) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.error,
                    )
                }
            }

            OutlinedTextField(
                value = form.name,
                onValueChange = { onEvent(ActivitiesUiEvent.OnNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.activities_name_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.activities_type_label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                SegmentedToggle(
                    options = types.map { stringResource(it.titleResId) },
                    selectedIndex = types.indexOf(form.type),
                    onOptionSelected = { index -> onEvent(ActivitiesUiEvent.OnTypeChanged(types[index])) },
                )
            }
        }

        SaveButton(
            enabled = form.canSave,
            onClick = { onEvent(ActivitiesUiEvent.OnFormSave) },
        )
    }
}

@PreviewLightDark
@Composable
private fun ActivityFormContentPreview() {
    WellyTheme {
        ActivityFormContent(
            form = ActivityFormState(
                id = 1,
                name = "Full body workout",
                type = ActivityType.Strength,
                isNameValid = true,
            ),
            onEvent = {},
        )
    }
}
