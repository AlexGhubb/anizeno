package com.example.anizeno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.anizeno.ui.theme.AnizenoTheme
import com.example.anizeno.view.core.AppModule
import com.example.anizeno.view.core.navigation.NavigationWrapper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppModule.initialize(application)
        enableEdgeToEdge()
        setContent {
            AnizenoTheme {
                NavigationWrapper()
            }
        }
    }
}
