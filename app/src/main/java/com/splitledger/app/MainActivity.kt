package com.splitledger.app

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
import androidx.core.view.WindowCompat
import com.splitledger.app.data.preferences.TokenManager
import com.splitledger.app.navigation.AppNavigation
import com.splitledger.app.ui.theme.SplitLedgerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val tokenManager = TokenManager(applicationContext)
        setContent {
            SplitLedgerTheme {
                AppNavigation(tokenManager = tokenManager)
            }
        }
    }
}