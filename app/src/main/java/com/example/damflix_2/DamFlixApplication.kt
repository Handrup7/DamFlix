package com.example.damflix_2

import android.app.Application
import com.example.damflix_2.Data.local.AppDatabase
import com.example.damflix_2.Data.PeliculaRepository

class DAMFlixApplication : Application() {
    // 1. La Base de Datos solo se crea cuando se acceda a ella por primera vez
    val database by lazy { AppDatabase.getDatabase(this) }
    // 2. El Repositorio se crea cuando se necesite, recibiendo el DAO de la BD de forma perezosa (lazy)
    val repository by lazy { PeliculaRepository(database.peliculaDao()) }
}
