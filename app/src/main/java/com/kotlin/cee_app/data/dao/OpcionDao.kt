package com.kotlin.cee_app.data.dao

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

    @Query("DELETE FROM opciones WHERE votacionId = :votacionId")
    suspend fun deleteByVotacionId(votacionId: String)

    @Query("SELECT * FROM opciones WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): OpcionEntity?

    @Query(
        "SELECT o.* FROM opciones o LEFT JOIN votos v ON o.id = v.opcionId " +
            "WHERE o.votacionId = :votacionId " +
            "GROUP BY o.id ORDER BY COUNT(v.id) DESC LIMIT 1"
    )
    suspend fun getWinnerForVotacion(votacionId: String): OpcionEntity?
}
