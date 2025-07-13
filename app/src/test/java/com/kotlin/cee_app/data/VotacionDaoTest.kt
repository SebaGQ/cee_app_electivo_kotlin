package com.kotlin.cee_app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.kotlin.cee_app.data.dao.VotacionDao
import com.kotlin.cee_app.data.dao.OpcionDao
import com.kotlin.cee_app.data.dao.VotoDao
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

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
}
