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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onGoLogin: () -> Unit,
    onRegisterClick: (String) -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    LaunchedEffect (Unit) {
        authViewModel.limpiarCampos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = com.example.poolcontrol.R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Crear Usuario",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = authViewModel.nombre,
                onValueChange = { authViewModel.nombre = it },
                label = { Text(text = "Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = authViewModel.errorNombre != null && authViewModel.nombre.isNotEmpty(),
                supportingText = {
                    authViewModel.errorNombre?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // CAMPO APELLIDO
            OutlinedTextField(
                value = authViewModel.apellido,
                onValueChange = { authViewModel.apellido = it },
                label = { Text(text = "Apellido") },
                modifier = Modifier.fillMaxWidth(),
                isError = authViewModel.errorApellido != null && authViewModel.apellido.isNotEmpty(),
                supportingText = {
                    authViewModel.errorApellido?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // CAMPO EMAIL
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = authViewModel.errorEmail != null && authViewModel.email.isNotEmpty(),
                supportingText = {
                    authViewModel.errorEmail?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // CAMPO CONTRASEÑA
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = { Text(text = "Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = authViewModel.errorPassword != null && authViewModel.password.isNotEmpty(),
                supportingText = {
                    authViewModel.errorPassword?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // CAMPO TELÉFONO
            OutlinedTextField(
                value = authViewModel.numero,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it.length <= 9) authViewModel.numero = it
                },
                label = { Text(text = "Número de teléfono") },
                modifier = Modifier.fillMaxWidth(),
                isError = authViewModel.errorNumero != null && authViewModel.numero.isNotEmpty(),
                supportingText = {
                    authViewModel.errorNumero?.let {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // BOTÓN REGISTRAR
            OutlinedButton(
                onClick = { onRegisterClick(authViewModel.email) },
                modifier = Modifier.fillMaxWidth(),
                enabled = authViewModel.formularioValido
            ) {
                Text(text = "Registrar")
            }

            Button(
                onClick = onGoLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver")
            }
        }
    }
}

