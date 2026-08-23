package com.example.conectaaula.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.example.conectaaula.tv.ui.theme.ConectaAulaTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConectaAulaTheme {
                ConectaAulaTvScreen()
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

    var respuestaCorrecta by remember {
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

                respuestaCorrecta =
                    snapshot.child("respuestaCorrecta")
                        .getValue(String::class.java)
                        ?: ""

                respuestaSeleccionada =
                    snapshot.child("respuesta")
                        .getValue(String::class.java)
                        ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                pregunta = "No se pudo recibir la pregunta"
            }
        }

        database.addValueEventListener(listener)

        onDispose {
            database.removeEventListener(listener)
        }
    }

    val resultadoDisponible =
        respuestaSeleccionada.isNotEmpty() &&
                respuestaCorrecta.isNotEmpty()

    val esCorrecta =
        resultadoDisponible &&
                respuestaSeleccionada == respuestaCorrecta

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101622))
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(0.84f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF4E72B1),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    )
            ) {

                Text(
                    text = "ConectaAula TV",
                    fontSize = 30.sp,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Actividad recibida desde el dispositivo móvil",
                    fontSize = 15.sp,
                    color = Color(0xFFE7EEFF)
                )
            }

            // Pregunta y opciones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF202632),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {

                Text(
                    text = "Pregunta",
                    fontSize = 16.sp,
                    color = Color(0xFFA7BFF0)
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = pregunta,
                    fontSize = 28.sp,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Button(
                        onClick = {
                            database
                                .child("respuesta")
                                .setValue("A")
                        },
                        modifier = Modifier.weight(1f)
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
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "B. $opcionB"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Button(
                        onClick = {
                            database
                                .child("respuesta")
                                .setValue("C")
                        },
                        modifier = Modifier.weight(1f)
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
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "D. $opcionD"
                        )
                    }
                }
            }

            // Resultado
            if (resultadoDisponible) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color =
                                if (esCorrecta) {
                                    Color(0xFF145234)
                                } else {
                                    Color(0xFF5A2525)
                                },
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            horizontal = 18.dp,
                            vertical = 12.dp
                        )
                ) {

                    if (esCorrecta) {

                        Text(
                            text = "✓ Respuesta correcta",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFADF0BF)
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Opción seleccionada: $respuestaSeleccionada",
                            fontSize = 16.sp,
                            color = Color(0xFFADF0BF)
                        )

                    } else {

                        Text(
                            text = "✗ Respuesta incorrecta",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFB4AB)
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Seleccionada: $respuestaSeleccionada   •   Correcta: $respuestaCorrecta",
                            fontSize = 16.sp,
                            color = Color(0xFFFFDAD6)
                        )
                    }
                }
            }
        }
    }
}