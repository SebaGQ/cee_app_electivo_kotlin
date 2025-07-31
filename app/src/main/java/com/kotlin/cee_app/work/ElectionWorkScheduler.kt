package com.kotlin.cee_app.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object ElectionWorkScheduler {
    private const val CLOSE_WORK_NAME = "closeExpiredElections"
    private const val OPEN_WORK_NAME = "openScheduledElections"

    internal fun nextDelayMinutes(now: LocalDateTime = LocalDateTime.now()): Long {
        val nextRun = now.plusMinutes(1)
        return Duration.between(now, nextRun).toMinutes()
    }

    internal fun nextDayDelayMinutes(now: LocalDateTime = LocalDateTime.now()): Long {
        val nextRun = now.toLocalDate().plusDays(1).atStartOfDay().plusMinutes(1)
        return Duration.between(now, nextRun).toMinutes()
    }

    fun scheduleDailyWork(context: Context) {
        scheduleCloseWork(context)
        scheduleOpenWork(context)
    }

    private fun scheduleCloseWork(context: Context) {
        val delay = nextDelayMinutes()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<CloseExpiredElectionsWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CLOSE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    private fun scheduleOpenWork(context: Context) {
        val delay = nextDayDelayMinutes()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<OpenScheduledElectionsWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            OPEN_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }
}
