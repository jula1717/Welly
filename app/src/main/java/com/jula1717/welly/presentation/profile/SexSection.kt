package com.jula1717.welly.presentation.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.presentation.components.SectionLabelWithInfo
import com.jula1717.welly.ui.theme.WellyTheme

private val SEX_PILL_OPTIONS = listOf(BiologicalSex.Female, BiologicalSex.Male)

@Composable
internal fun SexSection(
    selected: BiologicalSex,
    onSelected: (BiologicalSex) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        SectionLabelWithInfo(
            label = stringResource(R.string.profile_sex_label),
            infoDescription = stringResource(R.string.cd_sex_info),
            infoTitle = stringResource(R.string.profile_sex_info_title),
            infoBody = stringResource(R.string.profile_sex_info_body),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SEX_PILL_OPTIONS.forEach { sex ->
                val isSelected = sex == selected
                SexPill(
                    label = stringResource(sex.labelRes()),
                    selected = isSelected,
                    // Tapping the already-selected pill clears the choice back to Unspecified.
                    onClick = { onSelected(if (isSelected) BiologicalSex.Unspecified else sex) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SexPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val container = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val content = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

   Box(
        modifier = modifier
            .heightIn(min = 52.dp)
            .clip(CircleShape)
            .background(container)
            .border(1.5.dp, borderColor, CircleShape)
            .selectable(
                selected = selected,
                interactionSource = null,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            ).padding(horizontal = 6.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = content,
        )
    }
}

@StringRes
private fun BiologicalSex.labelRes(): Int =
    when (this) {
        BiologicalSex.Female -> R.string.profile_sex_female
        BiologicalSex.Male -> R.string.profile_sex_male
        BiologicalSex.Unspecified -> R.string.profile_sex_unspecified
    }

@PreviewLightDark
@Composable
private fun SexSectionPreview() {
    WellyTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SexSection(selected = BiologicalSex.Female, onSelected = {})
        }
    }
}
