package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.poolcontrol.ui.components.AppTopBar
import com.example.poolcontrol.ui.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReserva(
    navController: NavHostController,
    reservaViewModel: ReservaViewModel, // Agregado para leer fechas de la DB
    onBack: () -> Unit,
    esAdmin: Boolean = false,
    piscinaId: Int
) {
    // Cargamos las fechas ocupadas desde la DB al iniciar la pantalla
    LaunchedEffect(piscinaId) {
        reservaViewModel.cargarFechasOcupadas(piscinaId)
    }

    val fechasOcupadas = reservaViewModel.fechasBloqueadas

    // Configuración del DatePicker con bloqueo dinámico
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // 1. Obtener la medianoche de hoy en UTC para comparar justamente
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val hoyMedianoche = calendar.timeInMillis

                // 2. Permitir si es hoy o el futuro (>= hoyMedianoche)
                if (utcTimeMillis < hoyMedianoche) return false

                // 3. Bloquear fechas que ya están en la base de datos (ocupadas)
                val fechaEvaluada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC") // Importante: usar misma zona horaria
                }.format(Date(utcTimeMillis))

                return !fechasOcupadas.contains(fechaEvaluada)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
    )

    val bg = MaterialTheme.colorScheme.surfaceVariant

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
                text = "AGREGAR RESERVAS",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            // Calendario
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
                        todayDateBorderColor = Color.Red,
                        disabledDayContentColor = Color.Gray // Días ocupados o pasados
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Fecha formateada para enviar a la siguiente pantalla (DB)
            val selectedDateText = datePickerState.selectedDateMillis?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
            } ?: ""

            // Cuadro informativo visual para el usuario
            Surface(
                color = Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayDate = datePickerState.selectedDateMillis?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                } ?: "Ninguna"

                Text(
                    text = "Fecha seleccionada: $displayDate",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            // Botón Siguiente
            Button(
                onClick = {
                    navController.navigate("ConfirmarReserva/$selectedDateText/$piscinaId/$esAdmin")
                },
                enabled = selectedDateText.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Siguiente", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Botón Volver
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Volver")
            }
        }
    }
}