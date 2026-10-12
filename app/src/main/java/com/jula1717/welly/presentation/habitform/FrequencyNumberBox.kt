package com.jula1717.welly.presentation.habitform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

private const val MAX_DIGITS = 3

@Composable
internal fun FrequencyNumberBox(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    onFocus: () -> Unit,
    rowSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(9.dp)
    val colors = MaterialTheme.colorScheme
    val containerColor = if (rowSelected) colors.surfaceContainerLowest else colors.background
    val borderColor = if (rowSelected) colors.surfaceContainerLowest else colors.outlineVariant

    BasicTextField(
        value = value?.toString().orEmpty(),
        onValueChange = { input -> onValueChange(input.filter(Char::isDigit).take(MAX_DIGITS).toIntOrNull()) },
        modifier = modifier
            .width(46.dp)
            .onFocusChanged { if (it.isFocused) onFocus() },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(
            color = colors.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        ),
        cursorBrush = SolidColor(colors.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 30.dp)
                    .clip(shape)
                    .background(containerColor)
                    .border(1.dp, borderColor, shape)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                innerTextField()
            }
        },
    )
}

@PreviewLightDark
@Composable
private fun FrequencyNumberBoxPreview() {
    WellyTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FrequencyNumberBox(value = null, onValueChange = {}, onFocus = {}, rowSelected = false)
            FrequencyNumberBox(value = 14, onValueChange = {}, onFocus = {}, rowSelected = false)
        }
    }
}
