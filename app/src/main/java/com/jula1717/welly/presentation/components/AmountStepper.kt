package com.jula1717.welly.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun AmountStepper(
    amountMl: Int,
    onAmountMlChange: (Int) -> Unit,
    hint: String,
    unitLabel: String,
    minAmountMl: Int,
    maxAmountMl: Int,
    modifier: Modifier = Modifier,
    quickAmountsMl: List<Int> = listOf(250, 330, 500),
    stepMl: Int = 10,
) {
    var amountText by rememberSaveable { mutableStateOf(amountMl.toString()) }

    LaunchedEffect(amountMl) {
        if (amountText.toIntOrNull() != amountMl) {
            amountText = amountMl.toString()
        }
    }

    val displaySmall = MaterialTheme.typography.displaySmall
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val amountTextStyle = remember(displaySmall, onTertiaryContainer) {
        displaySmall.copy(
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = onTertiaryContainer,
            textAlign = TextAlign.Center,
        )
    }

    fun updateAmount(newAmount: Int) {
        val clampedAmount = newAmount.coerceIn(
            minAmountMl,
            maxAmountMl,
        )

        amountText = clampedAmount.toString()
        onAmountMlChange(clampedAmount)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleIconButton(
                icon = Icons.Default.Remove,
                contentDescription = "-$stepMl$unitLabel",
                onClick = {
                    updateAmount(amountMl - stepMl)
                },
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier.width(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (amountText.isEmpty()) {
                            Text(
                                text = amountMl.toString(),
                                style = amountTextStyle.copy(
                                    color = onTertiaryContainer.copy(alpha = 0.4f),
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        BasicTextField(
                            value = amountText,
                            onValueChange = { input ->
                                val digits = input.filter(Char::isDigit)

                                amountText = digits

                                if (digits.isNotEmpty()) {
                                    digits.toIntOrNull()?.let { value ->
                                        onAmountMlChange(
                                            value.coerceIn(
                                                minAmountMl,
                                                maxAmountMl,
                                            ),
                                        )
                                    }
                                }
                            },
                            textStyle = amountTextStyle,
                            singleLine = true,
                            cursorBrush = SolidColor(
                                MaterialTheme.colorScheme.tertiary,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Text(
                        text = unitLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(
                            start = 6.dp,
                            bottom = 4.dp,
                        ),
                    )
                }

                Text(
                    text = hint,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }

            CircleIconButton(
                icon = Icons.Default.Add,
                contentDescription = "+$stepMl$unitLabel",
                onClick = {
                    updateAmount(amountMl + stepMl)
                },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            quickAmountsMl.forEach { quickAmount ->
                val selected = amountMl == quickAmount

                FilterChip(
                    selected = selected,
                    onClick = {
                        updateAmount(quickAmount)
                    },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                    ),
                    label = {
                        Text(
                            text = "$quickAmount $unitLabel",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AmountStepperPreview() {
    WellyTheme {
        AmountStepper(
            amountMl = 330,
            onAmountMlChange = {},
            hint = "Tap to type an amount",
            unitLabel = "ml",
            minAmountMl = 0,
            maxAmountMl = 5000,
        )
    }
}
