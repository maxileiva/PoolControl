package com.example.poolcontrol
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.poolcontrol.navigation.AppNavGraph
import com.example.poolcontrol.navigation.Route
import com.example.poolcontrol.ui.screen.AddReserva
import com.example.poolcontrol.ui.screen.ConsultaReserva
import com.example.poolcontrol.ui.screen.DashboardAdmin
import com.example.poolcontrol.ui.screen.DashboardCliente
import com.example.poolcontrol.ui.screen.LoginScreen
import com.example.poolcontrol.ui.screen.RegisterScreen

import com.example.poolcontrol.ui.theme.PoolControlTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoolControlTheme {
                    AppRoot()
                }
            }
            }
        }

@Composable
fun AppRoot(){
    //crear un cotrolador de navegación principal
    val navController = rememberNavController()
    MaterialTheme {
            AppNavGraph(navController = navController)
        }
    }
