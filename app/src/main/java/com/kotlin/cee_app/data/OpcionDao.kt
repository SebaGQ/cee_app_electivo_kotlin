package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OpcionDao {
    @Insert
    suspend fun insert(opcion: OpcionEntity): Long

    @Query("SELECT * FROM opciones")
    fun getAll(): Flow<List<OpcionEntity>>

    @Query("SELECT * FROM opciones WHERE votacionId = :votacionId")
    fun getByVotacionId(votacionId: String): Flow<List<OpcionEntity>>
}
