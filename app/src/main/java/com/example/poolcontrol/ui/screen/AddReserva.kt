package com.example.poolcontrol.ui.screen

import android.health.connect.datatypes.units.BloodGlucose
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.poolcontrol.navigation.Route
import com.example.poolcontrol.ui.components.AppTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReserva() {
    // Color de fondo cian para toda la pantalla

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

            // Tarjeta del Calendario (Gris con tus colores específicos)
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
                        // Añadimos estos para asegurar que los nombres de los días se vean
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
                onClick = { /* Lógica de reserva */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Confirmar Reserva", color = Color.White   )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}