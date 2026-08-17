package com.jula1717.welly.presentation.today

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.GlassWater
import com.composables.icons.lucide.Lucide
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
internal fun AddEntryOptions(
    visible: Boolean,
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() +
            scaleIn(initialScale = 0.6f, transformOrigin = TransformOrigin(1f, 1f)),
        exit = fadeOut() +
            scaleOut(targetScale = 0.6f, transformOrigin = TransformOrigin(1f, 1f)),
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MiniFabOption(
                icon = Icons.Filled.Restaurant,
                label = stringResource(R.string.today_add_meal),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = onAddMeal,
                modifier = Modifier.fillMaxWidth(),
            )
            MiniFabOption(
                icon = Lucide.GlassWater,
                label = stringResource(R.string.today_add_drink),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = onAddDrink,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddEntryOptionsPreview() {
    WellyTheme {
        AddEntryOptions(
            visible = true,
            onAddMeal = {},
            onAddDrink = {},
        )
    }
}
