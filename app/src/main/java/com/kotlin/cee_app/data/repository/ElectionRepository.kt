package com.kotlin.cee_app.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import com.kotlin.cee_app.data.AppDatabase
import com.kotlin.cee_app.data.dao.VotacionWithCount
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import com.kotlin.cee_app.data.model.ConteoOpcion

/**
 * Acceso a datos de votaciones y opciones.
 */
class ElectionRepository private constructor(private val db: AppDatabase) {

    val votaciones: Flow<List<VotacionEntity>> = db.votacionDao().getAll()

    fun opcionesDeVotacion(votacionId: String): Flow<List<OpcionEntity>> =
        db.opcionDao().getByVotacionId(votacionId)

    suspend fun insertarVotacion(votacion: VotacionEntity) =
        db.votacionDao().insert(votacion)

    suspend fun actualizarVotacion(votacion: VotacionEntity) =
        db.votacionDao().update(votacion)

    suspend fun eliminarVotacion(id: String) {
        db.votoDao().deleteByVotacionId(id)
        db.opcionDao().deleteByVotacionId(id)
        db.votacionDao().deleteById(id)
    }

    suspend fun eliminarOpciones(votacionId: String) =
        db.opcionDao().deleteByVotacionId(votacionId)

    suspend fun obtenerVotacion(id: String) =
        db.votacionDao().findById(id)

    suspend fun insertarOpcion(opcion: OpcionEntity) =
        db.opcionDao().insert(opcion)

    suspend fun obtenerOpcion(id: Long): OpcionEntity? =
        db.opcionDao().findById(id)

    suspend fun insertarVoto(voto: VotoEntity): Boolean {
        val count = db.votoDao().countByVotacionAndUsuario(voto.votacionId, voto.usuarioId)
        return if (count > 0) {
            false
        } else {
            db.votoDao().insert(voto)
            true
        }
    }

    suspend fun haVotado(votacionId: String, usuarioId: String): Boolean =
        db.votoDao().countByVotacionAndUsuario(votacionId, usuarioId) > 0

    suspend fun opcionVotadaPorUsuario(votacionId: String, usuarioId: String): Long? =
        db.votoDao().opcionIdByVotacionAndUsuario(votacionId, usuarioId)

    suspend fun contarVotos(votacionId: String) =
        db.votoDao().countByVotacion(votacionId)

    suspend fun opcionGanadora(votacionId: String): OpcionEntity? =
        db.opcionDao().getWinnerForVotacion(votacionId)

    suspend fun resultados(votacionId: String): List<ConteoOpcion> =
        db.votoDao().conteoPorOpcion(votacionId)

    suspend fun totalUsuarios() = db.usuarioDao().countAll()

    // Nuevos métodos para el dashboard mejorado
    suspend fun obtenerVotosConteosPorVotacion(): Map<String, Int> {
        val votacionesList = db.votacionDao().getVoteCountByVotacion()
        return votacionesList.associate { it.id to it.count }
    }

    suspend fun obtenerVotacionesConConteo(): List<VotacionWithCount> =
        db.votacionDao().getVotacionesWithVoteCount()

    suspend fun contarVotacionesPorEstado(estado: String): Int =
        db.votacionDao().countByEstado(estado)

    companion object {
        @Volatile private var INSTANCE: ElectionRepository? = null

        fun getInstance(context: Context): ElectionRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = ElectionRepository(AppDatabase.getDatabase(context))
                INSTANCE = repo
                repo
            }
        }
    }
}