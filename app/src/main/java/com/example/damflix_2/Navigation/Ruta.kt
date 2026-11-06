package com.example.damflix_2.Navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.List
import com.example.damflix_2.R

// Clase cerrada que define las diferentes pantallas de la aplicación.
// Cada pantalla tiene una ruta, un recurso para su etiqueta y un icono.
sealed class Ruta(
    val path: String,
    val label: Int,
    val icon: ImageVector
) {

    // Pantalla que muestra el catálogo de películas.
    object Listado : Ruta(
        path = "listado",
        label = R.string.nav_movies,
        icon = Icons.AutoMirrored.Filled.List
    )

    // Pantalla que permite añadir una película.
    object Formulario : Ruta(
        path = "alta",
        label = R.string.nav_add,
        icon = Icons.Filled.Add
    )

    // Pantalla que muestra información sobre la aplicación.
    object Informacion : Ruta(
        path = "informacion",
        label = R.string.nav_information,
        icon = Icons.Filled.Info
    )

    // Pantalla que permite editar una película existente.
    // No aparece en el menú: se abre desde la lista.
    object Editar : Ruta(
        path = "editar",
        label = R.string.nav_edit,
        icon = Icons.Filled.Edit
    )

    // Ruta dinámica con parámetro de ID (en minúsculas).
    // No aparece en el menú: se abre al tocar una tarjeta de la lista.
    object Detalle : Ruta(
        path = "detalle/{peliculaId}",
        label = R.string.nav_detail,
        icon = Icons.Filled.Details
    ) {
        fun crearRuta(peliculaId: Int) = "detalle/$peliculaId"
    }
}
