package com.example.poolcontrol.ui.screen
import android.R
import android.net.wifi.hotspot2.pps.HomeSp
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.navigation.Route
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable

fun RegisterScreen(
    onGoHome: () -> Unit,
    onGoLogin: () -> Unit
) {

    val bg = MaterialTheme.colorScheme.surfaceVariant

    var email by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //contenedor pantalla completa
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg) //color de fondo
            .padding(16.dp), // margenes interiores
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = com.example.poolcontrol.R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Text(text = "Crear Usuario",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold)


            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text (text = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text (text = "Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text (text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text (text = "Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text (text = "Numero de telefono") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(onClick = onGoHome) { Text(text = "Registrar") }
            Button(onClick = onGoHome) { Text(text = "Volver") }

        }
        }
    }
