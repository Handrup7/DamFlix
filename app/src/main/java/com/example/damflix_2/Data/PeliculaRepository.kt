package com.example.damflix_2.Data


import com.example.damflix_2.Data.local.PeliculaDao
import com.example.damflix_2.Data.local.PeliculaEntity
import kotlinx.coroutines.flow.Flow


/**
 * Repositorio encargado de gestionar las películas en SQLite.
 *
 * La interfaz no accede directamente a la base de datos:
 * llama a este repositorio para consultar, insertar,
 * actualizar o eliminar películas.
 */
class PeliculaRepository(private val dao: PeliculaDao) {

    val todasPeliculas: Flow<List<PeliculaEntity>> = dao.obtenerTodas()
    suspend fun insertar(pelicula: PeliculaEntity) {
        dao.insertar(pelicula)
    }
    suspend fun actualizar(pelicula: PeliculaEntity) {
        dao.actualizar(pelicula)
    }
    suspend fun eliminar(pelicula: PeliculaEntity) {
        dao.eliminar(pelicula)
    }
}
