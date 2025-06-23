package com.kotlin.cee_app.repository

import android.content.Context
import com.kotlin.cee_app.data.AppDatabase
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.data.VotoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio centralizado para operaciones de votaciones.
 */
class ElectionsRepository private constructor(private val db: AppDatabase) {

    private val votacionDao = db.votacionDao()
    private val opcionDao = db.opcionDao()
    private val votoDao = db.votoDao()
    private val usuarioDao = db.usuarioDao()

    fun getVotaciones(): Flow<List<VotacionEntity>> = votacionDao.getAll()

    suspend fun countVotos(votacionId: String): Int =
        votoDao.countByVotacion(votacionId)

    fun getOpciones(votacionId: String) = opcionDao.getByVotacion(votacionId)

    suspend fun countUsuarios(): Int = usuarioDao.count()

    suspend fun insertVotacion(votacion: VotacionEntity) =
        votacionDao.insert(votacion)

    suspend fun insertOpcion(opcion: OpcionEntity) =
        opcionDao.insert(opcion)

    suspend fun insertVoto(voto: VotoEntity) =
        votoDao.insert(voto)

    companion object {
        @Volatile
        private var INSTANCE: ElectionsRepository? = null

        fun getInstance(context: Context): ElectionsRepository =
            INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                ElectionsRepository(db).also { INSTANCE = it }
            }
    }
}
