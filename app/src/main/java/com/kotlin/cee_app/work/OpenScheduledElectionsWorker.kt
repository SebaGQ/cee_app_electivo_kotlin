package com.kotlin.cee_app.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kotlin.cee_app.data.entity.EstadoVotacion
import com.kotlin.cee_app.data.repository.ElectionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class OpenScheduledElectionsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repo = ElectionRepository.getInstance(applicationContext)
        val today = LocalDate.now()
        repo.votaciones
            .first()
            .filter { it.estado == EstadoVotacion.CERRADA && it.fechaInicio.isEqual(today) }
            .forEach { repo.actualizarVotacion(it.copy(estado = EstadoVotacion.ABIERTA)) }
        return Result.success()
    }
}
