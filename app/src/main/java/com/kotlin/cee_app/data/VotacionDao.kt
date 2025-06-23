package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VotacionDao {
    @Insert
    suspend fun insert(votacion: VotacionEntity)

    @Update
    suspend fun update(votacion: VotacionEntity)

    @Query("DELETE FROM votaciones WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM votaciones")
    fun getAll(): Flow<List<VotacionEntity>>

    @Query("SELECT * FROM votaciones WHERE id = :id")
    suspend fun findById(id: String): VotacionEntity?
}
