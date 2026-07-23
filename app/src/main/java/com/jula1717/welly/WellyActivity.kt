package com.jula1717.welly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.jula1717.welly.presentation.navigation.WellyNavHost
import com.jula1717.welly.ui.theme.WellyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WellyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WellyTheme {
                WellyNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
