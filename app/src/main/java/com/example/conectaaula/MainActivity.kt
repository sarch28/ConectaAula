package com.example.conectaaula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectaaula.ui.theme.ConectaAulaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ConectaAulaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConectaAulaScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConectaAulaScreen(modifier: Modifier = Modifier) {

    var pregunta by rememberSaveable { mutableStateOf("") }
    var opcionA by rememberSaveable { mutableStateOf("") }
    var opcionB by rememberSaveable { mutableStateOf("") }
    var opcionC by rememberSaveable { mutableStateOf("") }
    var opcionD by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "ConectaAula",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Crea una pregunta para mostrarla en la Smart TV",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pregunta,
            onValueChange = { pregunta = it },
            label = { Text("Pregunta") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = opcionA,
            onValueChange = { opcionA = it },
            label = { Text("Opción A") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = opcionB,
            onValueChange = { opcionB = it },
            label = { Text("Opción B") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = opcionC,
            onValueChange = { opcionC = it },
            label = { Text("Opción C") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = opcionD,
            onValueChange = { opcionD = it },
            label = { Text("Opción D") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                mensaje = "Pregunta lista para enviar a la TV"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Enviar a TV")
        }

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConectaAulaPreview() {
    ConectaAulaTheme {
        ConectaAulaScreen()
    }
}