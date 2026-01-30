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
    reservaViewModel: ReservaViewModel,
    onBack: () -> Unit,
    esAdmin: Boolean = false,
    piscinaId: Int
) {
    // 1. Cargamos las fechas ocupadas desde la API al iniciar la pantalla
    // Ahora sin parámetros porque la API trae todas las reservas
    LaunchedEffect(Unit) {
        reservaViewModel.cargarFechasOcupadas()
    }

    val fechasOcupadas = reservaViewModel.fechasBloqueadas

    // 2. Configuración del DatePicker
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // No permitir fechas pasadas
                if (utcTimeMillis < calendar.timeInMillis) return false

                // Formatear para comparar con lo que viene de la API (yyyy-MM-dd)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val fechaEvaluada = sdf.format(Date(utcTimeMillis))

                // Si la fecha está en la lista de bloqueadas, no es seleccionable
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
                text = "SELECCIONAR FECHA",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            // Calendario visual
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
                        selectedDayContainerColor = Color(0xFF4CAF50), // Verde para selección
                        selectedDayContentColor = Color.White,
                        weekdayContentColor = Color.White,
                        todayContentColor = Color.White,
                        todayDateBorderColor = Color.White,
                        disabledDayContentColor = Color.Gray
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Formateo de fecha para el envío (IMPORTANTE usar UTC aquí también)
            val selectedDateText = datePickerState.selectedDateMillis?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.format(Date(it))
            } ?: ""

            // Información visual
            Surface(
                color = Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayDate = datePickerState.selectedDateMillis?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }.format(Date(it))
                } ?: "Ninguna"

                Text(
                    text = "Fecha: $displayDate",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            // Botón Siguiente
            Button(
                onClick = {
                    // Asegúrate de que tu ruta en el NavGraph soporte estos argumentos
                    navController.navigate("ConfirmarReserva/$selectedDateText/$piscinaId/$esAdmin")
                },
                enabled = selectedDateText.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("SIGUIENTE", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("VOLVER")
            }
        }
    }
}