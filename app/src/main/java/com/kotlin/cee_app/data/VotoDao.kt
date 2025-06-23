package com.kotlin.cee_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VotoDao {
    @Insert
    suspend fun insert(voto: VotoEntity): Long

    @Query("SELECT * FROM votos")
    fun getAll(): Flow<List<VotoEntity>>

    @Query("SELECT COUNT(*) FROM votos WHERE opcionId IN (SELECT id FROM opciones WHERE votacionId = :votacionId)")
    suspend fun countByVotacion(votacionId: String): Int

    @Query("DELETE FROM votos WHERE opcionId IN (SELECT id FROM opciones WHERE votacionId = :votacionId)")
    suspend fun deleteByVotacionId(votacionId: String)
}
