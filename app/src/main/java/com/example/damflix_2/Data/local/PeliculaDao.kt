package com.example.damflix_2.Data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PeliculaDao {
    // Al retornar Flow, Room notifica automáticamente cualquier cambio en la tabla
    @Query("SELECT * FROM peliculas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<PeliculaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(peliculaEntity: PeliculaEntity)

    @Update
    suspend fun actualizar(peliculaEntity: PeliculaEntity)

    @Delete
    suspend fun eliminar(peliculaEntity: PeliculaEntity)
}