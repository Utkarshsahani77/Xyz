package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.UtkarshMusicApp
import com.example.ui.theme.UtkarshMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtkarshMusicTheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UtkarshMusicApp()
                }
            }
        }
    }
}
