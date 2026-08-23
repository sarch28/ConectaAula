package com.example.conectaaula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectaaula.ui.theme.ConectaAulaTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ConectaAulaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF5F8FC)
                ) { innerPadding ->

                    ConectaAulaScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConectaAulaScreen(
    modifier: Modifier = Modifier
) {

    var pregunta by rememberSaveable {
        mutableStateOf("")
    }

    var opcionA by rememberSaveable {
        mutableStateOf("")
    }

    var opcionB by rememberSaveable {
        mutableStateOf("")
    }

    var opcionC by rememberSaveable {
        mutableStateOf("")
    }

    var opcionD by rememberSaveable {
        mutableStateOf("")
    }

    var respuestaCorrecta by rememberSaveable {
        mutableStateOf("")
    }

    var respuestaCorrectaFirebase by rememberSaveable {
        mutableStateOf("")
    }

    var respuestaTv by rememberSaveable {
        mutableStateOf("")
    }

    var mensaje by rememberSaveable {
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

                respuestaTv =
                    snapshot.child("respuesta")
                        .getValue(String::class.java)
                        ?: ""

                respuestaCorrectaFirebase =
                    snapshot.child("respuestaCorrecta")
                        .getValue(String::class.java)
                        ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                respuestaTv = ""
                respuestaCorrectaFirebase = ""
            }
        }

        database.addValueEventListener(listener)

        onDispose {
            database.removeEventListener(listener)
        }
    }

    val resultadoDisponible =
        respuestaTv.isNotEmpty() &&
                respuestaCorrectaFirebase.isNotEmpty()

    val esCorrecta =
        resultadoDisponible &&
                respuestaTv == respuestaCorrectaFirebase

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FC))
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // Encabezado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4568A6)
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 14.dp
                )
            ) {

                Text(
                    text = "ConectaAula",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Actividad interactiva para Smart TV",
                    color = Color(0xFFE6EEFF),
                    fontSize = 13.sp
                )
            }
        }

        // Tarjeta del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Título + botón Limpiar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Crear pregunta",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334E78),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(
                        onClick = {

                            database
                                .removeValue()
                                .addOnSuccessListener {

                                    pregunta = ""
                                    opcionA = ""
                                    opcionB = ""
                                    opcionC = ""
                                    opcionD = ""

                                    respuestaCorrecta = ""
                                    respuestaCorrectaFirebase = ""
                                    respuestaTv = ""

                                    mensaje = "Actividad limpiada"
                                }
                                .addOnFailureListener {

                                    mensaje =
                                        "No se pudo limpiar la actividad"
                                }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Text(
                            text = "Limpiar",
                            color = Color(0xFF4568A6),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    text = "Escribe la pregunta, las opciones y marca la respuesta correcta.",
                    fontSize = 12.sp,
                    color = Color(0xFF5F6B7A)
                )

                OutlinedTextField(
                    value = pregunta,
                    onValueChange = {
                        pregunta = it
                    },
                    label = {
                        Text("Pregunta")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = opcionA,
                    onValueChange = {
                        opcionA = it
                    },
                    label = {
                        Text("Opción A")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = opcionB,
                    onValueChange = {
                        opcionB = it
                    },
                    label = {
                        Text("Opción B")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = opcionC,
                    onValueChange = {
                        opcionC = it
                    },
                    label = {
                        Text("Opción C")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = opcionD,
                    onValueChange = {
                        opcionD = it
                    },
                    label = {
                        Text("Opción D")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Respuesta correcta",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334E78)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    listOf(
                        "A",
                        "B",
                        "C",
                        "D"
                    ).forEach { opcion ->

                        if (respuestaCorrecta == opcion) {

                            Button(
                                onClick = {
                                    respuestaCorrecta = opcion
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(opcion)
                            }

                        } else {

                            OutlinedButton(
                                onClick = {
                                    respuestaCorrecta = opcion
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(opcion)
                            }
                        }
                    }
                }

                // Enviar pregunta
                Button(
                    onClick = {

                        if (
                            pregunta.isBlank() ||
                            opcionA.isBlank() ||
                            opcionB.isBlank() ||
                            opcionC.isBlank() ||
                            opcionD.isBlank()
                        ) {

                            mensaje =
                                "Completa todos los campos"

                        } else if (
                            respuestaCorrecta.isBlank()
                        ) {

                            mensaje =
                                "Selecciona la respuesta correcta"

                        } else {

                            val datosPregunta = mapOf(
                                "pregunta" to pregunta,
                                "opcionA" to opcionA,
                                "opcionB" to opcionB,
                                "opcionC" to opcionC,
                                "opcionD" to opcionD,
                                "respuestaCorrecta" to respuestaCorrecta,
                                "respuesta" to ""
                            )

                            database
                                .setValue(datosPregunta)
                                .addOnSuccessListener {

                                    mensaje =
                                        "Pregunta enviada correctamente"

                                    respuestaTv = ""

                                    respuestaCorrectaFirebase =
                                        respuestaCorrecta
                                }
                                .addOnFailureListener {

                                    mensaje =
                                        "No se pudo enviar la pregunta"
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4568A6),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Enviar pregunta a TV",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mensaje.isNotEmpty()) {

                    Text(
                        text = mensaje,
                        fontSize = 13.sp,
                        color = Color(0xFF4568A6),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Respuesta recibida desde la TV
        if (respuestaCorrectaFirebase.isNotEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (!resultadoDisponible || esCorrecta) {
                            Color(0xFFE4F5EA)
                        } else {
                            Color(0xFFFDE8E8)
                        }
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = "Respuesta de la TV",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color =
                            if (!resultadoDisponible || esCorrecta) {
                                Color(0xFF276749)
                            } else {
                                Color(0xFFB3261E)
                            }
                    )

                    if (respuestaTv.isEmpty()) {

                        Text(
                            text = "Esperando respuesta...",
                            fontSize = 13.sp,
                            color = Color(0xFF4F6F5D)
                        )

                    } else if (esCorrecta) {

                        Text(
                            text = "✓ Respuesta correcta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF238636)
                        )

                        Text(
                            text = "Opción seleccionada: $respuestaTv",
                            fontSize = 14.sp,
                            color = Color(0xFF276749)
                        )

                    } else {

                        Text(
                            text = "✗ Respuesta incorrecta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB3261E)
                        )

                        Text(
                            text =
                                "Seleccionada: $respuestaTv   •   Correcta: $respuestaCorrectaFirebase",
                            fontSize = 14.sp,
                            color = Color(0xFF8C1D18)
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConectaAulaPreview() {

    ConectaAulaTheme {

        Text(
            text = "ConectaAula",
            modifier = Modifier.padding(24.dp)
        )
    }
}