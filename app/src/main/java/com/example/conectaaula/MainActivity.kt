package com.example.conectaaula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    var mensaje by rememberSaveable {
        mutableStateOf("")
    }

    var respuestaTv by rememberSaveable {
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
            }

            override fun onCancelled(error: DatabaseError) {
                respuestaTv = ""
            }
        }

        database.addValueEventListener(listener)

        onDispose {
            database.removeEventListener(listener)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F8FC))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4568A6)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text = "ConectaAula",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Actividad interactiva para Smart TV",
                    color = Color(0xFFE6EEFF),
                    fontSize = 15.sp
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Crear pregunta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334E78)
                )

                Text(
                    text = "Escribe una pregunta y cuatro opciones para enviarlas a la televisión.",
                    style = MaterialTheme.typography.bodyMedium,
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
                    shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Button(
                    onClick = {

                        if (
                            pregunta.isBlank() ||
                            opcionA.isBlank() ||
                            opcionB.isBlank() ||
                            opcionC.isBlank() ||
                            opcionD.isBlank()
                        ) {

                            mensaje = "Completa todos los campos"

                        } else {

                            val datosPregunta = mapOf(
                                "pregunta" to pregunta,
                                "opcionA" to opcionA,
                                "opcionB" to opcionB,
                                "opcionC" to opcionC,
                                "opcionD" to opcionD,
                                "respuesta" to ""
                            )

                            database
                                .setValue(datosPregunta)
                                .addOnSuccessListener {

                                    mensaje =
                                        "Pregunta enviada correctamente"

                                    respuestaTv = ""
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
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text(
                        text = "Enviar pregunta a TV",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mensaje.isNotEmpty()) {

                    Text(
                        text = mensaje,
                        color = Color(0xFF4568A6),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE4F5EA)
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Respuesta de la TV",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF276749)
                )

                if (respuestaTv.isEmpty()) {

                    Text(
                        text = "Esperando una respuesta desde la Smart TV...",
                        fontSize = 15.sp,
                        color = Color(0xFF4F6F5D)
                    )

                } else {

                    Text(
                        text = "Opción seleccionada: $respuestaTv",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF238636)
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}