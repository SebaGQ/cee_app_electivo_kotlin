package com.kotlin.cee_app.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import com.kotlin.cee_app.data.repository.ElectionRepository
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import com.kotlin.cee_app.data.entity.UsuarioEntity
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class ElectionRepositoryHasVotedTest {
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
    fun hasVoted_returns_correct_value() = runBlocking {
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
        val opcionId = db.opcionDao().insert(
            OpcionEntity(descripcion = "A", votacionId = "v1")
        )
        db.votoDao().insert(
            VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = opcionId,
                usuarioId = "u1",
                votacionId = "v1"
            )
        )

        assertTrue(repo.haVotado("v1", "u1"))
        assertFalse(repo.haVotado("v1", "u2"))
    }
}
