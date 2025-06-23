package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios")
    fun getAll(): Flow<List<UsuarioEntity>>
}
