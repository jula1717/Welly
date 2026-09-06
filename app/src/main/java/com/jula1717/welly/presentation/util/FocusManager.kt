package com.jula1717.welly.presentation.util

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun ClearFocusOnResume() {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

/**
 * Drops focus when the soft keyboard is dismissed (system back / swipe-down) while [focused], so
 * a text field finishes editing just as it would on a tap elsewhere. The "was shown" latch stops
 * a field that gains focus before the IME has animated up from being cleared straight away.
 *
 * [focused] is passed in rather than tracked here so a caller that already observes its own focus
 * state (interaction source, `onFocusChanged`) stays the single source of truth.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClearFocusOnKeyboardHidden(focused: Boolean) {
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.isImeVisible
    var imeWasShown by remember { mutableStateOf(false) }
    LaunchedEffect(imeVisible) {
        if (imeVisible) {
            imeWasShown = true
        } else if (imeWasShown) {
            imeWasShown = false
            if (focused) focusManager.clearFocus()
        }
    }
}
