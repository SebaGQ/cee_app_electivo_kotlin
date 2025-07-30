package com.kotlin.cee_app.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kotlin.cee_app.data.repository.ElectionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class CloseExpiredElectionsWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repo = ElectionRepository.getInstance(applicationContext)
        val today = LocalDate.now()
        val list = repo.votaciones.first()
        list.filter { it.fechaFin.isEqual(today) && it.estado != "Finalizada" }
            .forEach { repo.finalizarVotacion(it) }
        return Result.success()
    }
}
