package com.example.damflix_2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.damflix_2.Data.PeliculaRepository

class PeliculasViewModelFactory(private val repository: PeliculaRepository)  : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
    // Validamos que se nos pida instanciar nuestro PeliculasViewModel
        if (PeliculasViewModel::class.java.isAssignableFrom(modelClass)) {
            return PeliculasViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel no reconocida por la fábrica: $modelClass")
    }

}