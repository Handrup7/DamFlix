package com.example.damflix_2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix_2.Data.PeliculaRepository
import com.example.damflix_2.Data.local.PeliculaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel del catálogo de películas.
 *
 * Convierte el Flow reactivo de Room en un StateFlow observable para la UI
 * y expone las acciones del usuario (añadir, editar o eliminar películas,
 * marcar favorita). Las operaciones de escritura se ejecutan en
 * Dispatchers.IO para no bloquear el hilo principal.
 */
class PeliculasViewModel(private val repository: PeliculaRepository) : ViewModel() {

    // Convierte el Flow reactivo de Room en un StateFlow que Compose
    // puede recolectar. Emite la lista vacía como valor inicial y se
    // actualiza automáticamente cada vez que cambian los datos.
    val listaPeliculas: StateFlow<List<PeliculaEntity>> = repository.todasPeliculas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Añade una película nueva a la base de datos.
     */
    fun agregarPelicula(titulo: String, director: String, añoEstreno: Int, esVista: Boolean) {
        viewModelScope.launch {
            // Creamos la entidad con los datos recibidos del formulario.
            val nueva = PeliculaEntity(
                id = 0,
                titulo = titulo,
                director = director,
                añoEstreno = añoEstreno,
                esVista = esVista,
                esFavorita = false
            )

            // Insertamos la película en un hilo de I/O (evita ANR).
            withContext(Dispatchers.IO) {
                repository.insertar(nueva)
            }
        }
    }

    /**
     * Cambia el estado de favorita de una película.
     */
    fun conmutarFavorito(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            // Copiamos la película cambiando solo la bandera esFavorita
            // (no mutamos el objeto original).
            val actualizada = pelicula.copy(esFavorita = !pelicula.esFavorita)

            // Actualizamos el registro en un hilo de I/O.
            withContext(Dispatchers.IO) {
                repository.actualizar(actualizada)
            }
        }
    }

    // Película que se está editando en este momento, si la hay.
    // La pantalla de edición la lee para rellenar los campos del formulario.
    var peliculaEnEdicion: PeliculaEntity? = null

    // Guarda la película que se va a editar y permite navegar a su formulario.
    fun seleccionarParaEditar(pelicula: PeliculaEntity) {
        peliculaEnEdicion = pelicula
    }

    /**
     * Elimina la película de la base de datos.
     */
    fun eliminarPelicula(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            // Eliminamos el registro en un hilo de I/O.
            withContext(Dispatchers.IO) {
                repository.eliminar(pelicula)
            }
        }
    }

    /**
     * Actualiza los datos de una película existente.
     * Recibe la película original y los valores nuevos del formulario de edición.
     */
    fun editarPelicula(
        pelicula: PeliculaEntity,
        titulo: String,
        director: String,
        añoEstreno: Int,
        esVista: Boolean
    ) {
        viewModelScope.launch {
            // Conservamos el identificador y las propiedades que no se editan.
            val actualizada = pelicula.copy(
                titulo = titulo,
                director = director,
                añoEstreno = añoEstreno,
                esVista = esVista
            )

            // Actualizamos el registro en un hilo de I/O.
            withContext(Dispatchers.IO) {
                repository.actualizar(actualizada)
            }
        }
    }
}
