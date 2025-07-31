package com.kotlin.cee_app.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import com.kotlin.cee_app.data.repository.ElectionRepository
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import com.kotlin.cee_app.data.entity.UsuarioEntity
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class ElectionRepositoryUserVoteTest {
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
    fun get_user_vote_returns_option_id() = runBlocking {
        val votacion = VotacionEntity(
            id = "v1",
            titulo = "t",
            descripcion = "d",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = EstadoVotacion.ABIERTA,
            adminId = "a1",
            finalParticipantCount = null,
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

        val result = repo.opcionVotadaPorUsuario("v1", "u1")
        assertEquals(opcionId, result)
        assertNull(repo.opcionVotadaPorUsuario("v1", "u2"))
    }
}
