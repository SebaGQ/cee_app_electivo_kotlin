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

    @Query(
        "SELECT v.*, COUNT(vt.id) as voteCount " +
                "FROM votaciones v " +
                "LEFT JOIN votos vt ON v.id = vt.votacionId " +
                "GROUP BY v.id " +
                "ORDER BY voteCount DESC"
    )
    suspend fun getVotacionesWithVoteCount(): List<VotacionWithCount>

    @Query("SELECT COUNT(*) FROM votaciones WHERE estado = :estado")
    suspend fun countByEstado(estado: String): Int

    @Query(
        "SELECT v.id, COUNT(vt.id) as count " +
                "FROM votaciones v " +
                "LEFT JOIN votos vt ON v.id = vt.votacionId " +
                "GROUP BY v.id"
    )
    suspend fun getVoteCountByVotacion(): List<VotacionVoteCount>
}

data class VotacionWithCount(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val estado: String,
    val adminId: String,
    val voteCount: Int
)

data class VotacionVoteCount(
    val id: String,
    val count: Int
)