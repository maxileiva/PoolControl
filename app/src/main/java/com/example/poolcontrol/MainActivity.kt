package com.example.poolcontrol

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
import com.example.poolcontrol.navigation.Route
import com.example.poolcontrol.ui.screen.HomeScreen
import com.example.poolcontrol.ui.screen.LoginScreen
import com.example.poolcontrol.ui.theme.PoolControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoolControlTheme {
                    HomeScreen(

                        onGoLogin = {},
                        onGoAddReserva = {}

                    )
            }
            }
        }
    }