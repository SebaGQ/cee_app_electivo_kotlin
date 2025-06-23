package com.kotlin.cee_app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class VotacionDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: VotacionDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.votacionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query() = runBlocking {
        val votacion = VotacionEntity(
            id = "v1",
            titulo = "Eleccion",
            descripcion = "desc",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now().plusDays(1),
            estado = "ABIERTA",
            adminId = "a1",
        )
        dao.insert(votacion)
        val list = dao.getAll().first()
        assertEquals(1, list.size)
        assertEquals("Eleccion", list.first().titulo)
    }

    @Test
    fun flow_emits_after_second_insert() = runBlocking {
        val v1 = VotacionEntity(
            id = "v1",
            titulo = "A",
            descripcion = "d",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "ABIERTA",
            adminId = "a1",
        )
        dao.insert(v1)
        assertEquals(1, dao.getAll().first().size)

        val v2 = VotacionEntity(
            id = "v2",
            titulo = "B",
            descripcion = "d",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "ABIERTA",
            adminId = "a1",
        )
        dao.insert(v2)
        val list = dao.getAll().first()
        assertEquals(2, list.size)
    }
}
