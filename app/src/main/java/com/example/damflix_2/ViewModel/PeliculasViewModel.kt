package com.example.damflix_2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damflix_2.Data.PeliculaRepository
import com.example.damflix_2.Data.local.PeliculaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeliculasViewModel(private val repository: PeliculaRepository) : ViewModel() {

    // Conversión automática del Flow de Room en StateFlow reactivo para Compose
    val listaPeliculas: StateFlow<List<PeliculaEntity>> = repository.todasPeliculas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarPelicula(titulo: String, director: String, añoEstreno: Int, esVista: Boolean) {
        viewModelScope.launch {
            // Creamos el objeto con los datos recibidos del formulario.
            val nueva = PeliculaEntity(
                id = 0,
                titulo = titulo,
                director = director,
                añoEstreno = añoEstreno,
                esVista = esVista,
                esFavorita = false
            )

            // Insertamos la película en la base de datos.
            repository.insertar(nueva)
        }
    }

    fun conmutarFavorito(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            val actualizada = pelicula.copy(esFavorita = !pelicula.esFavorita)
            repository.actualizar(actualizada)
        }
    }

    // Película que se está editando en este momento, si la hay.
    // La pantalla de edición la lee para rellenar los campos del formulario.
    var peliculaEnEdicion: PeliculaEntity? = null

    // Guarda la película que se va a editar y permite navegar a su formulario.
    fun seleccionarParaEditar(pelicula: PeliculaEntity) {
        peliculaEnEdicion = pelicula
    }

    // Elimina la película de la base de datos.
    fun eliminarPelicula(pelicula: PeliculaEntity) {
        viewModelScope.launch {
            repository.eliminar(pelicula)
        }
    }

    // Actualiza los datos de una película existente.
    // Recibe la película original y los valores nuevos del formulario de edición.
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
            repository.actualizar(actualizada)
        }
    }
}
