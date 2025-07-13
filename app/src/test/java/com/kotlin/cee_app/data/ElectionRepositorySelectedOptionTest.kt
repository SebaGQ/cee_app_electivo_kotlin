package com.kotlin.cee_app.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class ElectionRepositorySelectedOptionTest {
    private lateinit var repo: ElectionRepository
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repo = ElectionRepository.getInstance(context)
        db = AppDatabase.getDatabase(context)
        db.clearAllTables()
    }

    @Test
    fun selected_option_returns_correct_value() = runBlocking {
        val votacion = VotacionEntity(
            id = "v1",
            titulo = "t",
            descripcion = "d",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "Abierta",
            adminId = "a1",
        )
        db.votacionDao().insert(votacion)
        val opcionA = db.opcionDao().insert(OpcionEntity(descripcion = "A", votacionId = "v1"))
        val opcionB = db.opcionDao().insert(OpcionEntity(descripcion = "B", votacionId = "v1"))
        db.votoDao().insert(
            VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = opcionB,
                usuarioId = "u1",
                votacionId = "v1"
            )
        )

        val selected = repo.opcionSeleccionada("v1", "u1")
        assertEquals(opcionB, selected)
        assertNull(repo.opcionSeleccionada("v1", "u2"))
    }
}
