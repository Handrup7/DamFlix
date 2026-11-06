package com.example.damflix_2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.damflix_2.Data.PeliculaRepository

/**
 * Fábrica para crear DetallePeliculaViewModel.
 *
 * La función viewModel() por defecto solo sabe instanciar ViewModels con
 * constructor vacío. Como este ViewModel necesita el PeliculaRepository,
 * la fábrica le dice al framework cómo construirlo (inyectando la
 * dependencia) y permite conservarlo ante rotaciones de pantalla.
 */
class DetallePeliculaViewModelFactory(
    private val repository: PeliculaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Solo reconocemos nuestro ViewModel; el resto no lo creamos.
        if (modelClass.isAssignableFrom(DetallePeliculaViewModel::class.java)) {
            return DetallePeliculaViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel no conocida: ${modelClass.name}")
    }
}