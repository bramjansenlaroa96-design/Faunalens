package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.FaunaLensApp
import com.example.ui.theme.FaunaLensTheme

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { FaunaAppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaunaLensTheme {
                FaunaLensApp(container = appContainer)
            }
        }
    }
}
