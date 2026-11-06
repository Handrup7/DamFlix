package com.example.damflix_2.ViewModel

import com.example.damflix_2.Data.local.PeliculaEntity

/**
 * Estados posibles de la pantalla de detalle.
 *
 * Es una interfaz sellada (sealed interface): el compilador garantiza que
 * solo pueden existir estos tres estados. Al hacer un `when` sobre ella
 * obligamos a gestionar todos los casos, sin estados olvidados.
 */
sealed interface DetalleUiState {

    // Se muestra mientras se consulta la base de datos en segundo plano.
    object Cargando : DetalleUiState

    // Se muestra cuando la película existe: guarda la entidad encontrada.
    data class Exito(val pelicula: PeliculaEntity) : DetalleUiState

    // Se muestra cuando la película no existe: guarda el mensaje de error.
    data class Error(val mensaje: String) : DetalleUiState
}