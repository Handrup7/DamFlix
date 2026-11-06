package com.example.damflix_2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix_2.Data.PeliculaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel de la pantalla de detalle.
 *
 * Consulta el repositorio y publica el estado de la pantalla
 * (Cargando / Exito / Error) como un StateFlow que Compose recolecta.
 * Recibe el PeliculaRepository inyectado mediante su fábrica.
 */
class DetallePeliculaViewModel(
    private val repository: PeliculaRepository
) : ViewModel() {

    // Estado interno (mutable), visible solo dentro de esta clase.
    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)

    // Estado público: solo lectura para el exterior, nunca escritura.
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    /**
     * Carga la película cuyo id se recibe.
     *
     * Se lanza dentro de viewModelScope (se cancela al destruir el ViewModel)
     * y la consulta a Room se ejecuta en Dispatchers.IO para no bloquear
     * el hilo principal (evita ANR).
     */
    fun cargarDetallePelicula(peliculaId: Int) {
        viewModelScope.launch {
            // 1. Indicamos que la pantalla está cargando.
            _uiState.value = DetalleUiState.Cargando

            // 2. Consulta a Room fuera del Main Thread, en un hilo de I/O.
            val resultado = withContext(Dispatchers.IO) {
                delay(1000) // Retardo simulado para ver el indicador de carga.
                repository.obtenerPorId(peliculaId)
            }

            // 3. Publicamos el estado final según si se encontró o no.
            if (resultado != null) {
                _uiState.value = DetalleUiState.Exito(resultado)
            } else {
                _uiState.value = DetalleUiState.Error("Película no encontrada.")
            }
        }
    }
}