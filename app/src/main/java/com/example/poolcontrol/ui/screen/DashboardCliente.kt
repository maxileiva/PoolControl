package com.example.poolcontrol.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import android.text.LoginFilter
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.R
import com.example.poolcontrol.ui.components.AppTopBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun DashboardCliente(
        onGoLogin: () -> Unit,
        onGoAddReserva: () -> Unit,
        onGoPerfil: () -> Unit,
        onGoConsultaReserva: () -> Unit
    ) {
    Scaffold(
        topBar = {
            AppTopBar(
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Button(
                onClick = onGoAddReserva,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Añadir Reserva", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGoConsultaReserva,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Consultar Reservas", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGoPerfil,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Ver Perfil", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

