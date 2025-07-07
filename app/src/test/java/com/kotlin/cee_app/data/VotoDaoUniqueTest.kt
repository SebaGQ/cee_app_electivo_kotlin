package com.kotlin.cee_app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class VotoDaoUniqueTest {
    private lateinit var db: AppDatabase
    private lateinit var votacionDao: VotacionDao
    private lateinit var opcionDao: OpcionDao
    private lateinit var votoDao: VotoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        votacionDao = db.votacionDao()
        opcionDao = db.opcionDao()
        votoDao = db.votoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun only_one_vote_per_user() = runBlocking {
        val votacion = VotacionEntity(
            id = "v1",
            titulo = "Test",
            descripcion = "desc",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "Abierta",
            adminId = "a1"
        )
        votacionDao.insert(votacion)
        val opcion = opcionDao.insert(OpcionEntity(descripcion = "A", votacionId = "v1"))

        votoDao.insert(VotoEntity(LocalDate.now(), opcion, "u1", "v1"))
        try {
            votoDao.insert(VotoEntity(LocalDate.now(), opcion, "u1", "v1"))
        } catch (_: Exception) {
        }
        val count = votoDao.countByVotacion("v1")
        assertEquals(1, count)
    }
}
