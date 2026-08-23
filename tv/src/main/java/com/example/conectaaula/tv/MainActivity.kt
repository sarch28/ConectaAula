package com.example.conectaaula.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.conectaaula.tv.ui.theme.ConectaAulaTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConectaAulaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    ConectaAulaTvScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ConectaAulaTvScreen() {

    var pregunta by remember {
        mutableStateOf("Esperando pregunta desde el móvil...")
    }

    var opcionA by remember {
        mutableStateOf("")
    }

    var opcionB by remember {
        mutableStateOf("")
    }

    var opcionC by remember {
        mutableStateOf("")
    }

    var opcionD by remember {
        mutableStateOf("")
    }

    var respuestaSeleccionada by remember {
        mutableStateOf("")
    }

    val database = remember {
        FirebaseDatabase
            .getInstance(
                "https://conectaaula-dc7bc-default-rtdb.firebaseio.com"
            )
            .getReference("quiz")
    }

    DisposableEffect(database) {

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                pregunta =
                    snapshot.child("pregunta")
                        .getValue(String::class.java)
                        ?: "Esperando pregunta desde el móvil..."

                opcionA =
                    snapshot.child("opcionA")
                        .getValue(String::class.java)
                        ?: ""

                opcionB =
                    snapshot.child("opcionB")
                        .getValue(String::class.java)
                        ?: ""

                opcionC =
                    snapshot.child("opcionC")
                        .getValue(String::class.java)
                        ?: ""

                opcionD =
                    snapshot.child("opcionD")
                        .getValue(String::class.java)
                        ?: ""

                respuestaSeleccionada =
                    snapshot.child("respuesta")
                        .getValue(String::class.java)
                        ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                pregunta = "Error al recibir la pregunta"
            }
        }

        database.addValueEventListener(listener)

        onDispose {
            database.removeEventListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "ConectaAula TV",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Pregunta recibida desde el dispositivo móvil",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = pregunta,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Button(
                onClick = {
                    database
                        .child("respuesta")
                        .setValue("A")

                    respuestaSeleccionada = "A"
                },
                modifier = Modifier.width(320.dp)
            ) {
                Text(
                    text = "A. $opcionA"
                )
            }

            Button(
                onClick = {
                    database
                        .child("respuesta")
                        .setValue("B")

                    respuestaSeleccionada = "B"
                },
                modifier = Modifier.width(320.dp)
            ) {
                Text(
                    text = "B. $opcionB"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Button(
                onClick = {
                    database
                        .child("respuesta")
                        .setValue("C")

                    respuestaSeleccionada = "C"
                },
                modifier = Modifier.width(320.dp)
            ) {
                Text(
                    text = "C. $opcionC"
                )
            }

            Button(
                onClick = {
                    database
                        .child("respuesta")
                        .setValue("D")

                    respuestaSeleccionada = "D"
                },
                modifier = Modifier.width(320.dp)
            ) {
                Text(
                    text = "D. $opcionD"
                )
            }
        }

        if (respuestaSeleccionada.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "Respuesta seleccionada: $respuestaSeleccionada",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}