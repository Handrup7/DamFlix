package com.example.damflix_2.Data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Objeto de Acceso a Datos (DAO) de la tabla "peliculas".
 *
 * Room valida las consultas SQL en tiempo de compilación (KSP) y
 * genera la implementación (PeliculaDao_Impl). Las funciones marcadas
 * como suspend se ejecutan fuera del hilo principal.
 */
@Dao
interface PeliculaDao {

    // Devuelve todas las películas como Flow: Room emite
    // automáticamente la lista actualizada cuando cambia la tabla
    // (persistencia reactiva).
    @Query("SELECT * FROM peliculas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<PeliculaEntity>>

    // Inserta una película; si ya existiera un registro con ese id,
    // lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(peliculaEntity: PeliculaEntity)

    // Actualiza una película usando su clave primaria.
    @Update
    suspend fun actualizar(peliculaEntity: PeliculaEntity)

    // Elimina una película.
    @Delete
    suspend fun eliminar(peliculaEntity: PeliculaEntity)

    // Consulta específica por identificador único (la usa la
    // pantalla de detalle). Devuelve null si no existe esa película.
    @Query("SELECT * FROM peliculas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): PeliculaEntity?
}
