package com.salvarez.mimateyaandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.salvarez.mimateyaandroid.designsystem.DesignSystemShowcase
import com.salvarez.mimateyaandroid.designsystem.MimateYaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MimateYaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Temporary: the sample screen stays until the real screens arrive (step 7).
                    DesignSystemShowcase(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
