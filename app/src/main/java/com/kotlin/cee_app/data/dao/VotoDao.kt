package com.kotlin.cee_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kotlin.cee_app.data.entity.VotoEntity
import com.kotlin.cee_app.data.model.ConteoOpcion
import kotlinx.coroutines.flow.Flow

@Dao
interface VotoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(voto: VotoEntity): Long

    @Query("SELECT * FROM votos")
    fun getAll(): Flow<List<VotoEntity>>

    @Query("SELECT COUNT(*) FROM votos WHERE votacionId = :votacionId")
    suspend fun countByVotacion(votacionId: String): Int

    @Query(
        "SELECT COUNT(*) FROM votos WHERE votacionId = :votacionId AND usuarioId = :usuarioId"
    )
    suspend fun countByVotacionAndUsuario(votacionId: String, usuarioId: String): Int

    @Query(
        "SELECT opcionId FROM votos WHERE votacionId = :votacionId AND usuarioId = :usuarioId LIMIT 1"
    )
    suspend fun opcionIdByVotacionAndUsuario(votacionId: String, usuarioId: String): Long?

    @Query("DELETE FROM votos WHERE votacionId = :votacionId")
    suspend fun deleteByVotacionId(votacionId: String)

    @Query(
        "SELECT o.descripcion AS descripcion, COUNT(v.id) AS total FROM opciones o " +
            "LEFT JOIN votos v ON o.id = v.opcionId WHERE o.votacionId = :votacionId " +
            "GROUP BY o.id ORDER BY o.id"
    )
    suspend fun conteoPorOpcion(votacionId: String): List<ConteoOpcion>
}
