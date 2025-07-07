package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(usuario: UsuarioEntity)

    @Update
    suspend fun update(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun findById(id: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    fun getAll(): Flow<List<UsuarioEntity>>

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun countAll(): Int

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun findByCorreo(correo: String): UsuarioEntity?
}
