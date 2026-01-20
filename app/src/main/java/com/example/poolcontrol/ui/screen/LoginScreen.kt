package com.example.poolcontrol.ui.screen
import android.R
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.navigation.Route

@Composable

fun LoginScreen(
    onGoHome: () -> Unit,
    onGoRegister: () -> Unit,
    onGoLose: () -> Unit
){
    val bg = MaterialTheme.colorScheme.surfaceVariant

    //contenedor pantalla completa
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg) //color de fondo
            .padding(16.dp), // margenes interiores
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Login",
                     style = MaterialTheme.typography.headlineMedium,
                     fontWeight = FontWeight.ExtraBold
                )
                //espacio blanco reservado por mi
                Spacer(Modifier.width(8.dp))
                AssistChip(
                    onClick = {},
                    label = {Text("Boton Navegacion")}
                )
            } //fin de la fila (Row)
            Spacer(Modifier.width(16.dp))

            //CARD ELEVADO (con sombra)

            ElevatedCard(
                modifier = Modifier.fillMaxWidth() //ocupe el tamaño de la columna
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text (
                        text = "Texto en login",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Otro texto en distinto formato",
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }

            //espacio
            Spacer(Modifier.width(24.dp))
            Row(
               horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onGoHome) { Text (text = "al Home")}
                OutlinedButton(onClick = onGoRegister) {Text(text = "al registro") }
            }



        }
    }
}