package com.example.poolcontrol.ui.screen
import android.text.LoginFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.poolcontrol.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    onGoLogin: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("PoolControl") })
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "bienvenido app")
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Imagen app",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            Button(onClick = onGoLogin,
                    modifier = Modifier
                        .fillMaxWidth()
            ){ Text (text = "al Home")}
    }
}
}
