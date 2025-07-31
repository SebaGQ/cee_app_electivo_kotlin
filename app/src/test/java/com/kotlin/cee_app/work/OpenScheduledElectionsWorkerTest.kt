package com.kotlin.cee_app.work

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.testing.TestWorkerBuilder
import com.kotlin.cee_app.data.AppDatabase
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import java.time.LocalDate
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OpenScheduledElectionsWorkerTest {
    private lateinit var context: Context
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getDatabase(context)
        db.clearAllTables()
    }

    @Test
    fun worker_opens_today_elections() = runBlocking {
        val today = LocalDate.now()
        val closedToday = VotacionEntity(
            id = "v1",
            titulo = "t",
            descripcion = "d",
            fechaInicio = today,
            fechaFin = today,
            estado = EstadoVotacion.CERRADA,
            adminId = "a1",
            finalParticipantCount = null,
        )
        db.votacionDao().insert(closedToday)

        val closedFuture = VotacionEntity(
            id = "v2",
            titulo = "t2",
            descripcion = "d2",
            fechaInicio = today.plusDays(1),
            fechaFin = today.plusDays(1),
            estado = EstadoVotacion.CERRADA,
            adminId = "a1",
            finalParticipantCount = null,
        )
        db.votacionDao().insert(closedFuture)

        val worker = TestWorkerBuilder<OpenScheduledElectionsWorker>(
            context = context,
            executor = Executors.newSingleThreadExecutor(),
        ).build()

        worker.doWork()

        val opened = db.votacionDao().findById("v1")
        val stillClosed = db.votacionDao().findById("v2")
        assertEquals(EstadoVotacion.ABIERTA, opened?.estado)
        assertEquals(EstadoVotacion.CERRADA, stillClosed?.estado)
    }
}
