package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import android.text.LoginFilter
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.R
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.components.BottomAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DashboardAdmin(
    onGoAddReserva: () -> Unit,
    onGoPerfil: () -> Unit,
    onGoConsultaReserva: () -> Unit,
    onGoDashboardAdmin: () -> Unit

) {Scaffold(
    topBar = {
        AppTopBar(
        )
    },
    bottomBar = { BottomAppBar( onGoAddReserva = onGoAddReserva,
                                onGoPerfil = onGoPerfil,
                                onGoConsultaReserva = onGoConsultaReserva,
                                onGoDashboardAdmin = onGoDashboardAdmin
    )
    },
    // Posicionamos el botón abajo al centro
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
        FloatingActionButton(
            onClick = onGoAddReserva,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Añadir Reserva")
            }
        }
    }
) { innerPadding ->
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de Datos de Hoy (RF6)
        Text(
            text = "Reserva de Hoy",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Cliente: Juan Pérez", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Teléfono: +56 9 1234 5678", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección de Futuros Clientes / Agenda (RF7)
        Text(
            text = "Próximos Clientes",
            style = MaterialTheme.typography.titleMedium
        )

        // Usamos LazyColumn para que si hay muchos clientes, se pueda hacer scroll
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f) // Esto permite que la lista ocupe el espacio sobrante
        ) {
            items(5) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Viernes ${23 + index}", fontWeight = FontWeight.Bold)
                        Text(text = "Cliente: Familia Gómez")
                        Text(text = "6 personas", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Espacio extra al final para que el botón no tape el último item de la lista
        Spacer(modifier = Modifier.height(80.dp))
    }
}
}