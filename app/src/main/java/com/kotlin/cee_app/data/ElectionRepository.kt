package com.kotlin.cee_app.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

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

    suspend fun insertarVoto(voto: VotoEntity) =
        db.votoDao().insert(voto)

    suspend fun contarVotos(votacionId: String) =
        db.votoDao().countByVotacion(votacionId)

    suspend fun opcionGanadora(votacionId: String): OpcionEntity? =
        db.opcionDao().getWinnerForVotacion(votacionId)

    suspend fun totalUsuarios() = db.usuarioDao().countAll()

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
