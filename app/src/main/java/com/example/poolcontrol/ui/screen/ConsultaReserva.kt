package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaReserva() {
    Scaffold(
        topBar = {
            AppTopBar(
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Consulta Reservas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )



            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cliente: Juan Pérez",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Teléfono: +56 9 1234 5678",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}





