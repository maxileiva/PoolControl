package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel, // Nombre de parámetro unificado para el NavGraph
    onGoRegister: () -> Unit,
    onLoginClick: (String) -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // LOGO
            Image(
                painter = painterResource(id = com.example.poolcontrol.R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "PoolControl",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Control de acceso",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO EMAIL conectado al ViewModel
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = authViewModel.errorEmail != null && authViewModel.email.isNotEmpty(),
                supportingText = {
                    authViewModel.errorEmail?.let { mensaje ->
                        if (authViewModel.email.isNotEmpty()) {
                            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                singleLine = true
            )

            // CAMPO CONTRASEÑA conectado al ViewModel
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = authViewModel.errorPassword != null && authViewModel.password.isNotEmpty(),
                supportingText = {
                    authViewModel.errorPassword?.let { mensaje ->
                        if (authViewModel.password.isNotEmpty()) {
                            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                singleLine = true
            )

            // MENSAJE DE ERROR DE LA BASE DE DATOS (Si el login falla)
            authViewModel.mensajeError?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // BOTÓN LOGIN
            Button(
                onClick = { onLoginClick(authViewModel.email) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = authViewModel.loginValido
            ) {
                Text("Ingresar")
            }

            TextButton(onClick = onGoRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}