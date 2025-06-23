package com.kotlin.cee_app.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UsuarioDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: UsuarioDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.usuarioDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_query() = runBlocking {
        val usuario = UsuarioEntity(
            id = "u1",
            nombre = "Juan",
            correo = "juan@example.com",
            rol = "SIMPLE",
        )
        dao.insert(usuario)
        val list = dao.getAll().first()
        assertEquals(1, list.size)
        assertEquals("Juan", list.first().nombre)
    }
}
