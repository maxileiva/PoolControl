package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    authViewModel: AuthViewModel,
    onGoRegister: () -> Unit,
    onLoginClick: (String, String) -> Unit // Cambiado de (String) a (String, String)
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant

    LaunchedEffect (Unit) {
        authViewModel.limpiarCampos()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(bg).padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // LOGO y TÍTULOS (Se mantienen igual...)
            Image(
                painter = painterResource(id = com.example.poolcontrol.R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Text(text = "PoolControl", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(40.dp))

            // CAMPOS DE TEXTO (Se mantienen igual...)
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            authViewModel.mensajeError?.let { msg ->
                Text(text = msg, color = MaterialTheme.colorScheme.error)
            }

            // BOTÓN LOGIN - CORREGIDO PARA ENVIAR AMBOS DATOS
            Button(
                onClick = { onLoginClick(authViewModel.email, authViewModel.password) },
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