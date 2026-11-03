package com.example.damflix_2.ui.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.damflix_2.Data.local.PeliculaEntity
import com.example.damflix_2.R
import com.example.damflix_2.ViewModel.PeliculasViewModel

/**
 * Pantalla que muestra las películas guardadas.
 *
 * Observa el estado del ViewModel y permite cambiar
 * el estado de favorita, editar o eliminar cada película.
 */
@Composable
fun ListaPeliculas(
    viewModel: PeliculasViewModel,
    onEditarPelicula: (PeliculaEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    // Recogemos el StateFlow del ViewModel como un estado de Compose.
    // Cuando cambia la lista, la interfaz se actualiza automáticamente.
    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle()

    // Película elegida para eliminar, si la hay.
    // Si no es nula, se muestra el diálogo de confirmación.
    var peliculaAEliminar by remember { mutableStateOf<PeliculaEntity?>(null) }

    // Organizamos el contenido verticalmente.
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // Mostramos un mensaje cuando todavía no hay películas.
        if (peliculas.isEmpty()) {

            // Centramos el mensaje tanto vertical como horizontalmente.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.movie_list_empty),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            // LazyColumn crea los elementos a medida que se necesitan,
            // lo que resulta útil cuando la lista contiene muchas películas.
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Creamos un elemento visual por cada película.
                // Usamos su ID como clave para identificarla de forma estable.
                items(
                    items = peliculas,
                    key = { pelicula -> pelicula.id }
                ) { pelicula ->

                    // Tarjeta que contiene los datos de cada película.
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Colocamos la información y el botón en una fila.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Columna con el título, director y año de estreno.
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                // Nombre de la película.
                                Text(
                                    text = pelicula.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                // Director y año de estreno.
                                Text(
                                    text = stringResource(
                                        R.string.movie_director_year,
                                        pelicula.director,
                                        pelicula.añoEstreno
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                // Mostramos si la película está vista o no.
                                Text(
                                    text = stringResource(
                                        if (pelicula.esVista) {
                                            R.string.form_movie_seen
                                        } else {
                                            R.string.movie_not_seen
                                        }
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // Botón para marcar o desmarcar como favorita.
                            IconButton(
                                onClick = {
                                    // El ViewModel cambia el estado del favorito
                                    // y actualiza el registro en la base de datos.
                                    viewModel.conmutarFavorito(pelicula)
                                }
                            ) {
                                // Cambiamos el icono y su descripción
                                // dependiendo de si ya es favorita.
                                Icon(
                                    imageVector = if (pelicula.esFavorita) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },

                                    // Descripción accesible que cambia según el estado del favorito.
                                    contentDescription = stringResource(
                                        if (pelicula.esFavorita) {
                                            R.string.favorite_remove_description
                                        } else {
                                            R.string.favorite_add_description
                                        }
                                    ),

                                    // El corazón es rojo si es favorita y gris en caso contrario.
                                    tint = if (pelicula.esFavorita) Color.Red else Color.Gray
                                )
                            }

                            // Botón para editar la película.
                            IconButton(
                                onClick = {
                                    onEditarPelicula(pelicula)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription =
                                        stringResource(R.string.edit_button_description)
                                )
                            }

                            // Botón para eliminar la película.
                            IconButton(
                                onClick = {
                                    // Se abre la confirmación antes de borrar.
                                    peliculaAEliminar = pelicula
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription =
                                        stringResource(R.string.delete_button_description)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación antes de eliminar una película.
    val peliculaPendiente = peliculaAEliminar
    if (peliculaPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                // Cerrar el diálogo sin eliminar nada.
                peliculaAEliminar = null
            },
            title = {
                Text(stringResource(R.string.confirm_delete_title))
            },
            text = {
                Text(
                    stringResource(
                        R.string.confirm_delete_message,
                        peliculaPendiente.titulo
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // El ViewModel elimina la película de la base de datos.
                        viewModel.eliminarPelicula(peliculaPendiente)
                        peliculaAEliminar = null
                    }
                ) {
                    Text(stringResource(R.string.confirm_delete_confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        peliculaAEliminar = null
                    }
                ) {
                    Text(stringResource(R.string.confirm_delete_cancel))
                }
            }
        )
    }
}
