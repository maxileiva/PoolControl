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
// Importa tus validadores (asegúrate de que el package sea correcto)
import com.example.poolcontrol.domain.validation.validateNameLettersOnly
import com.example.poolcontrol.domain.validation.validatePhoneDigitsOnly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarReserva(
    fechaSeleccionada: String,
    onBack: () -> Unit,
    onConfirmar: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant

    // ESTADOS MANUALES (Sin depender del ViewModel por ahora)
    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cantidadPersonas by remember { mutableStateOf("") }

    // LÓGICA DE VALIDACIÓN
    val errorNombre = validateNameLettersOnly(nombreCompleto)
    val errorTelefono = validatePhoneDigitsOnly(telefono)

    // Validación: Mínimo 10, Máximo 15
    val errorCantidad = when {
        cantidadPersonas.isBlank() -> "Ingrese cantidad"
        cantidadPersonas.toIntOrNull() == null -> "Solo números"
        cantidadPersonas.toInt() < 10 -> "El mínimo es de 10 personas"
        else -> null
    }

    // El botón se habilita solo si todo está correcto
    val formularioValido = errorNombre == null &&
            errorTelefono == null &&
            errorCantidad == null &&
            nombreCompleto.isNotBlank()

    // Lista de piscinas (Asegúrate de tener estas imágenes en tu carpeta res/drawable)
    val listaPiscinas = listOf(
        Pair("Piscina Principal", R.drawable.piscina1),
        Pair("Piscina Familia Pequeña", R.drawable.piscina2)
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

            // --- SELECTOR DE PISCINA ---
            Text(text = "Seleccione el Recinto", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { if (piscinaSeleccionadaIndice > 0) piscinaSeleccionadaIndice-- }) { Text("<") }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = listaPiscinas[piscinaSeleccionadaIndice].second),
                        contentDescription = "Piscina",
                        modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = listaPiscinas[piscinaSeleccionadaIndice].first, fontWeight = FontWeight.Bold)
                }
                Button(onClick = { if (piscinaSeleccionadaIndice < listaPiscinas.size - 1) piscinaSeleccionadaIndice++ }) { Text(">") }
            }

            // --- CAMPOS DE TEXTO CON VALIDACIÓN ---

            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorNombre != null && nombreCompleto.isNotEmpty(),
                supportingText = {
                    if (nombreCompleto.isNotEmpty()) {
                        errorNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                }
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de contacto") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorTelefono != null && telefono.isNotEmpty(),
                supportingText = {
                    if (telefono.isNotEmpty()) {
                        errorTelefono?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                }
            )

            OutlinedTextField(
                value = cantidadPersonas,
                onValueChange = { cantidadPersonas = it },
                label = { Text("Cantidad (Mín. 10 - Máx. 15)") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorCantidad != null && cantidadPersonas.isNotEmpty(),
                supportingText = {
                    if (cantidadPersonas.isNotEmpty()) {
                        errorCantidad?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onConfirmar,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = formularioValido,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), disabledContainerColor = Color.Gray)
            ) {
                Text("FINALIZAR RESERVA", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("VOLVER ATRÁS")
            }
        }
    }
}