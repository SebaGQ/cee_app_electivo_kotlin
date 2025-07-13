package com.kotlin.cee_app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import com.kotlin.cee_app.data.dao.OpcionDao
import com.kotlin.cee_app.data.dao.VotacionDao
import com.kotlin.cee_app.data.dao.VotoDao
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class OpcionDaoWinnerTest {
    private lateinit var db: AppDatabase
    private lateinit var opcionDao: OpcionDao
    private lateinit var votacionDao: VotacionDao
    private lateinit var votoDao: VotoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        opcionDao = db.opcionDao()
        votacionDao = db.votacionDao()
        votoDao = db.votoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun winner_query_returns_option() = runBlocking {
        val votacion = VotacionEntity(
            id = "v1",
            titulo = "Test",
            descripcion = "desc",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "CERRADA",
            adminId = "a1",
            finalParticipantCount = null
        )
        votacionDao.insert(votacion)
        val idA = opcionDao.insert(OpcionEntity(descripcion = "A", votacionId = "v1"))
        val idB = opcionDao.insert(OpcionEntity(descripcion = "B", votacionId = "v1"))
        votoDao.insert(
            VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = idA,
                usuarioId = "u1",
                votacionId = "v1"
            )
        )
        votoDao.insert(
            VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = idA,
                usuarioId = "u2",
                votacionId = "v1"
            )
        )
        votoDao.insert(
            VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = idB,
                usuarioId = "u3",
                votacionId = "v1"
            )
        )

        val winner = opcionDao.getWinnerForVotacion("v1")
        assertEquals("A", winner?.descripcion)
    }
}
