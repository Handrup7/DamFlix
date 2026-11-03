package com.example.damflix_2.Data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Modelo que representa una película dentro de DAMFlix.
// Cada objeto Pelicula contiene los datos de una película.
@Entity(tableName = "peliculas")
data class PeliculaEntity(

    // Identificador único de la película.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Título de la película.
    val titulo: String,

    // Nombre de su director o directora.
    val director: String,

    // Año en el que se estrenó.
    val añoEstreno: Int,

    // Indica si el usuario ya ha visto la película.
    val esVista: Boolean = false,

    // Indica si la película está marcada como favorita.
    // Por defecto, una película no es favorita.
    val esFavorita: Boolean = false
)
