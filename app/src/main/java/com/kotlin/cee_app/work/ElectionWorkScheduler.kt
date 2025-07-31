package com.kotlin.cee_app.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import java.util.concurrent.TimeUnit

object ElectionWorkScheduler {
    private const val CLOSE_WORK_NAME = "closeExpiredElections"
    private const val OPEN_WORK_NAME = "openScheduledElections"

    /** Intervalo fijo para la ejecución periódica de los workers. */
    internal const val WORK_INTERVAL_MINUTES = 15L

    fun schedulePeriodicWork(context: Context) {
        scheduleCloseWork(context)
        scheduleOpenWork(context)
    }

    private fun scheduleCloseWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<CloseExpiredElectionsWorker>(
            WORK_INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CLOSE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    private fun scheduleOpenWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<OpenScheduledElectionsWorker>(
            WORK_INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            OPEN_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}
