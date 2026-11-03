package com.example.damflix_2.ui.View

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.damflix_2.R
import com.example.damflix_2.ViewModel.PeliculasViewModel

// Formulario para editar una película existente.
// La película a modificar la lleva el ViewModel.
@Composable
fun FormularioEditarPelicula(
    viewModel: PeliculasViewModel,
    onGuardada: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Película que vamos a editar, guardada en el ViewModel.
    val pelicula = viewModel.peliculaEnEdicion

    // Estados locales que guardan lo escrito en cada campo.
    // Se inician con los datos actuales de la película.
    // El año se mantiene como texto hasta validar su contenido.
    var titulo by remember(pelicula) { mutableStateOf(pelicula?.titulo ?: "") }
    var director by remember(pelicula) { mutableStateOf(pelicula?.director ?: "") }
    var añoTexto by remember(pelicula) { mutableStateOf(pelicula?.añoEstreno?.toString() ?: "") }
    var esVista by remember(pelicula) { mutableStateOf(pelicula?.esVista ?: false) }

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
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título de la pantalla.
        Text(
            text = stringResource(R.string.form_edit_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Espacio entre el título y el contenido.
        Spacer(modifier = Modifier.height(24.dp))

        // Si la película no se ha encontrado, lo comunicamos.
        if (pelicula == null) {
            Text(
                text = stringResource(R.string.movie_not_found),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {

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

            // Botón para guardar los cambios.
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

                        // El ViewModel actualiza la película en la base de datos.
                        viewModel.editarPelicula(
                            pelicula = pelicula,
                            titulo = titulo.trim(),
                            director = director.trim(),
                            añoEstreno = anio,
                            esVista = esVista
                        )

                        // Limpiamos la película en edición.
                        viewModel.peliculaEnEdicion = null

                        // Avisamos de que se han enviado los cambios.
                        // Como en el alta, no confirma que SQLite
                        // haya terminado de actualizar el registro.
                        Toast.makeText(
                            contexto,
                            R.string.movie_updated,
                            Toast.LENGTH_SHORT
                        ).show()

                        // Vuelve al catálogo.
                        onGuardada()
                    }
                },

                // El botón solo se habilita si todos los datos son válidos.
                enabled = esFormularioValido,
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
            ) {
                // Texto visible dentro del botón.
                Text(stringResource(R.string.form_edit_save))
            }
        }
    }
}
