package com.jula1717.welly.presentation.addmeal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.presentation.components.AiActionButton
import com.jula1717.welly.presentation.components.ParsedMacrosBar
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun AiMacroCard(
    macros: MealMacros?,
    macrosError: Boolean,
    onCopyPrompt: () -> Unit,
    onPasteJson: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.add_meal_macro_section_title).uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AiActionButton(
                    icon = Icons.Default.ContentCopy,
                    label = stringResource(R.string.add_meal_copy_prompt),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = onCopyPrompt,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                AiActionButton(
                    icon = Icons.Default.ContentPaste,
                    label = stringResource(R.string.add_meal_paste_json),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = onPasteJson,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
            if (macrosError) {
                Text(
                    text = stringResource(R.string.add_meal_macro_error),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        macros?.let {
            ParsedMacrosBar(macros = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AiMacroCardEmptyPreview() {
    WellyTheme {
        AiMacroCard(
            macros = null,
            macrosError = false,
            onCopyPrompt = {},
            onPasteJson = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiMacroCardWithMacrosPreview() {
    WellyTheme {
        AiMacroCard(
            macros = MealMacros(
                calories = 455,
                protein = 30,
                carbs = 50,
                fat = 15,
                fiber = 5,
            ),
            macrosError = false,
            onCopyPrompt = {},
            onPasteJson = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiMacroCardErrorPreview() {
    WellyTheme {
        AiMacroCard(
            macros = null,
            macrosError = true,
            onCopyPrompt = {},
            onPasteJson = {},
        )
    }
}
