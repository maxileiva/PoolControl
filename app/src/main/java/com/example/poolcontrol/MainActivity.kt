package com.example.poolcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.poolcontrol.data.local.database.AppDatabase
import com.example.poolcontrol.navigation.AppNavGraph
import com.example.poolcontrol.ui.theme.PoolControlTheme
import com.example.poolcontrol.data.repository.RolRepository


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de la base de datos
        val database = AppDatabase.getInstance(applicationContext)
        // Inicialización del repositorio usando el DAO de la base de datos
        val repository = RolRepository(database.rolDao())

        enableEdgeToEdge()
        setContent {
            PoolControlTheme {
                AppRoot(repository)
            }
        }
    }
}

@Composable
fun AppRoot(repository: RolRepository) {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Pasamos el navController y el repositorio al Grafo de Navegación
        AppNavGraph(navController = navController, repository = repository)
    }
}