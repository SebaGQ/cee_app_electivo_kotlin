package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VotacionDao {
    @Insert
    suspend fun insert(votacion: VotacionEntity)

    @Query("SELECT * FROM votaciones")
    fun getAll(): Flow<List<VotacionEntity>>
}
