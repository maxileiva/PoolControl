package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.components.AppTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReserva(
    navController: NavHostController,
    onBack: () -> Unit,
    esAdmin: Boolean = false
) {

    val datePickerState = rememberDatePickerState()
    val bg = MaterialTheme.colorScheme.surfaceVariant

    Scaffold(
        topBar = {
            AppTopBar(
            )
        }
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
                text = "AGREGAR RESERVAS",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF616161)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    title = null,
                    headline = null,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color(0xFF616161),
                        titleContentColor = Color.White,
                        dayContentColor = Color.White,
                        selectedDayContainerColor = Color.Red,
                        selectedDayContentColor = Color.White,
                        weekdayContentColor = Color.White,
                        todayContentColor = Color.White,
                        todayDateBorderColor = Color.Red
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Lógica de fecha seleccionada
            val selectedDateText = datePickerState.selectedDateMillis?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
            } ?: "Seleccione una fecha"

            // Cuadro informativo de la fecha
            Surface(
                color = Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fecha: $selectedDateText",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    val fechaSegura = selectedDateText.replace("/", "-")
                    // Pasamos la fecha y el booleano esAdmin en la URL
                    navController.navigate("ConfirmarReserva/$fechaSegura/$esAdmin")
                    },

                enabled = selectedDateText != "Seleccione una fecha",
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDateText != "Seleccione una fecha") Color(0xFF4CAF50) else Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Siguiente", color = Color.White)
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Volver")
            }
        }
    }
}