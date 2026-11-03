package com.example.damflix_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.damflix_2.Data.PeliculaRepository
import com.example.damflix_2.Data.local.AppDatabase
import com.example.damflix_2.Navigation.Navegacion
import com.example.damflix_2.ViewModel.PeliculasViewModel
import com.example.damflix_2.ViewModel.PeliculasViewModelFactory
import com.example.damflix_2.ui.theme.DamFlix_2Theme

// Actividad principal de la aplicación.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Obtenemos el contenedor global de la aplicación
        val app = application as DAMFlixApplication
        // 2. Creamos la fábrica pasándole el repositorio almacenado en la Application
        val factory = PeliculasViewModelFactory(app.repository)

        // 3. Construimos la interfaz con Jetpack Compose.
        setContent {
            // Aplicamos el tema visual de DAMFlix.
            DamFlix_2Theme {
                // 4. Compose obtiene y mantiene el ViewModel retenido ante rotaciones
                val viewModel: PeliculasViewModel = viewModel(factory = factory)
                // 5. Invocamos la pantalla principal pasando el ViewModel
                Navegacion(viewModel = viewModel)
            }
        }
    }
}
