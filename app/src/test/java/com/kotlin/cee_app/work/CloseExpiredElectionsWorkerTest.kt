package com.kotlin.cee_app.work

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.testing.TestWorkerBuilder
import com.kotlin.cee_app.data.AppDatabase
import com.kotlin.cee_app.data.entity.VotacionEntity
import java.time.LocalDate
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CloseExpiredElectionsWorkerTest {
    private lateinit var context: Context
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getDatabase(context)
        db.clearAllTables()
    }

    @Test
    fun worker_closes_due_elections() = runBlocking {
        val today = LocalDate.now()
        val open = VotacionEntity(
            id = "v1",
            titulo = "t",
            descripcion = "d",
            fechaInicio = today,
            fechaFin = today,
            estado = "Abierta",
            adminId = "a1",
            finalParticipantCount = null,
        )
        db.votacionDao().insert(open)

        val future = VotacionEntity(
            id = "v2",
            titulo = "t2",
            descripcion = "d2",
            fechaInicio = today,
            fechaFin = today.plusDays(1),
            estado = "Abierta",
            adminId = "a1",
            finalParticipantCount = null,
        )
        db.votacionDao().insert(future)

        val worker = TestWorkerBuilder<CloseExpiredElectionsWorker>(
            context = context,
            executor = Executors.newSingleThreadExecutor(),
        ).build()

        worker.doWork()

        assertEquals("Finalizada", db.votacionDao().findById("v1")?.estado)
        assertEquals("Abierta", db.votacionDao().findById("v2")?.estado)
    }
}
