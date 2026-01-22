package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.R
import com.example.poolcontrol.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarReserva(
    fechaSeleccionada: String, // Recibimos la fecha de la pantalla anterior
    onBack: () -> Unit,
    onConfirmar: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant

    // Estados para los campos de texto
    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cantidadPersonas by remember { mutableStateOf("") }

    // Lista de piscinas para el "carrusel" simple
    val listaPiscinas = listOf(
        Pair("Piscina Olímpica", R.drawable.logo), // Reemplaza con tus fotos reales
        Pair("Piscina Familiar", R.drawable.logo),
        Pair("Piscina VIP", R.drawable.logo)
    )
    var piscinaSeleccionadaIndice by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { AppTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "DETALLES DE RESERVA",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Resumen de la fecha traída
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF616161))
            ) {
                Text(
                    text = "Fecha: $fechaSeleccionada",
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Carrusel de selección de piscina
            Text(text = "Seleccione el Recinto", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    if (piscinaSeleccionadaIndice > 0) piscinaSeleccionadaIndice--
                }) { Text("<") }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = listaPiscinas[piscinaSeleccionadaIndice].second),
                        contentDescription = "Piscina",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = listaPiscinas[piscinaSeleccionadaIndice].first,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(onClick = {
                    if (piscinaSeleccionadaIndice < listaPiscinas.size - 1) piscinaSeleccionadaIndice++
                }) { Text(">") }
            }

            // Formulario
            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de contacto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = { cantidadPersonas = it },
                label = { Text("Cantidad de personas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botones Finales
            Button(
                onClick = onConfirmar,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde confirmación
            ) {
                Text("FINALIZAR RESERVA", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("VOLVER ATRÁS")
            }
        }
    }
}