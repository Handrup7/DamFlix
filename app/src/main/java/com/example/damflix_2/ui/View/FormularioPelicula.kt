package com.example.damflix_2.ui.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.damflix_2.R
import com.example.damflix_2.ViewModel.PeliculasViewModel

// Formulario para introducir una película nueva.
// Recibe el ViewModel que guarda las películas.
@Composable
fun FormularioPelicula(viewModel: PeliculasViewModel,onPeliculaGuardada: () -> Unit, modifier: Modifier = Modifier) {

    // Estados locales que guardan lo escrito en cada campo.
    // El año se mantiene como texto hasta validar su contenido.
    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var añoTexto by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }

    // Contexto necesario para mostrar mensajes Toast.
    val contexto = LocalContext.current

    // Convertimos el año a entero sin provocar errores si el texto
    // está vacío o contiene caracteres que no son números.
    val añoIntroducido = añoTexto.toIntOrNull()

    // Validamos que el año sea un número entre 1700 y 2026.
    val añoValido = añoIntroducido != null &&
            añoIntroducido in 1700..2026

    // Comprobamos que el título y el director no estén vacíos
    // y que el año se haya introducido correctamente.
    val esFormularioValido = titulo.isNotBlank() &&
            director.isNotBlank() &&
            añoValido

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título de la pantalla.
        Text(
            text = stringResource(R.string.form_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Espacio entre el título y la imagen.
        Spacer(modifier = Modifier.height(20.dp))

        // Imagen de perfil cargada desde los recursos drawable.
        Image(
            painter = painterResource(id = R.drawable.avatar_handrup),
            contentDescription = stringResource(R.string.info_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .border(3.dp, Color.Black, CircleShape)
        )

        // Separación entre la imagen y los campos.
        Spacer(modifier = Modifier.height(24.dp))

        // Campo para el título.
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text(stringResource(R.string.form_movie_title)) },
            singleLine = true,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        )

        // Campo para el director.
        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text(stringResource(R.string.form_director)) },
            singleLine = true,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        )

        // Campo para el año de estreno.
        OutlinedTextField(
            value = añoTexto,
            onValueChange = { añoTexto = it },
            label = { Text(stringResource(R.string.form_release_year)) },
            singleLine = true,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            // Marcamos el campo como erróneo solo cuando contiene
            // texto y el año no es válido.
            isError = añoTexto.isNotBlank() && !añoValido,

            // Mostramos el mensaje de error cuando corresponda.
            supportingText = {
                if (añoTexto.isNotBlank() && !añoValido) {
                    Text(stringResource(R.string.form_year_error))
                }
            }
        )

        // Checkbox para indicar si ya has visto la película.
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = esVista,
                onCheckedChange = { esVista = it }
            )

            Text(text = stringResource(R.string.form_movie_seen))
        }

        // Espacio entre la casilla y el botón.
        Spacer(modifier = Modifier.height(12.dp))

        // Botón para guardar la película.
        Button(
            onClick = {
                // Obtenemos el año validado. Si no es válido,
                // salimos sin intentar guardar la película.
                val anio = añoTexto.toIntOrNull()

                if (anio != null &&
                    anio in 1700..2026 &&
                    titulo.isNotBlank() &&
                    director.isNotBlank()
                ) {

                    // Añadimos la película al ViewModel.
                    // El ViewModel se encarga de guardarla mediante el repositorio y la base de datos.
                    viewModel.agregarPelicula(
                        titulo = titulo.trim(),
                        director = director.trim(),
                        añoEstreno = anio,
                        esVista = esVista
                    )
                    // Avisamos de que se ha enviado la petición
                    // de guardado. No confirma por sí solo que SQLite
                    // haya terminado de insertar el registro.
                    Toast.makeText(
                        contexto,
                        R.string.movie_saved,
                        Toast.LENGTH_SHORT
                    ).show()

                    // Limpiamos el formulario para introducir otra.
                    titulo = ""
                    director = ""
                    añoTexto = ""
                    esVista = false

                    // Ejecutamos la acción indicada por navegación.
                    onPeliculaGuardada()
                }
            },

            // El botón solo se habilita si todos los datos obligatorios son válidos.
            enabled = esFormularioValido && añoValido,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            // Texto visible dentro del botón.
            Text(stringResource(R.string.form_save_movie))
        }
    }
}
