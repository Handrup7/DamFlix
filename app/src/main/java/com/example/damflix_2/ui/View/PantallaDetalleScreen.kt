package com.example.damflix_2.ui.View

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.damflix_2.R
import com.example.damflix_2.ViewModel.DetallePeliculaViewModel
import com.example.damflix_2.ViewModel.DetalleUiState
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon

// Pantalla de detalle: muestra la ficha completa de una película.
// La carga se dispara una sola vez al entrar en la pantalla.
@Composable
fun PantallaDetalleScreen(
    peliculaId: Int,
    viewModel: DetallePeliculaViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Inicia la carga asíncrona al entrar en la pantalla
    // (o si cambia el id que se recibe).
    LaunchedEffect(peliculaId) {
        viewModel.cargarDetallePelicula(peliculaId)
    }

    // Observa el estado del ViewModel: la interfaz se recompone
    // automáticamente cuando el estado cambia.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón para volver al catálogo (event hoisting: la navegación
        // la decide el contenedor, no esta pantalla).
        // La flecha es un icono vectorial: no depende de la fuente y
        // nunca se recorta. Lo aliamos a ancho completo y con una
        // altura un poco mayor para que se vea clara.
        Button(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(R.string.detail_back_to_catalog))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gestiona cada uno de los posibles estados de la pantalla.
        when (val state = uiState) {
            is DetalleUiState.Cargando -> {
                // Centra el indicador de progreso mientras se consulta Room.
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.detail_loading))
                    }
                }
            }

            is DetalleUiState.Exito -> {
                // Tarjeta con la ficha completa de la película.
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = state.pelicula.titulo,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.detail_director,
                                state.pelicula.director
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(
                                R.string.detail_year,
                                state.pelicula.añoEstreno
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(
                                if (state.pelicula.esVista) {
                                    R.string.detail_status_seen
                                } else {
                                    R.string.detail_status_not_seen
                                }
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            is DetalleUiState.Error -> {
                Text(
                    text = state.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}