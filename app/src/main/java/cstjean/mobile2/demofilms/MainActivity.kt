package cstjean.mobile2.demofilms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cstjean.mobile2.demofilms.ui.DemoFilmsApp
import cstjean.mobile2.demofilms.ui.theme.DemoFilmsTheme
import cstjean.mobile2.demofilms.ui.screens.MediaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoFilmsTheme {
                DemoFilmsApp()
            }
        }
    }
}