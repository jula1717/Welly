package com.jula1717.welly.presentation.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jula1717.welly.R
import com.jula1717.welly.ui.theme.WellyTheme

@Composable
fun TodayDestination(
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TodayScreen(
        onAddMeal = onAddMeal,
        onAddDrink = onAddDrink,
        modifier = modifier,
    )
}

@Composable
fun TodayScreen(
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var fabExpanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.today_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        AddEntryScrim(
            visible = fabExpanded,
            onDismiss = { fabExpanded = false },
        )

        AddEntryFab(
            expanded = fabExpanded,
            onExpandedChange = { fabExpanded = it },
            onAddMeal = onAddMeal,
            onAddDrink = onAddDrink,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 16.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun TodayScreenPreview() {
    WellyTheme {
        TodayScreen(
            onAddMeal = {},
            onAddDrink = {},
        )
    }
}
