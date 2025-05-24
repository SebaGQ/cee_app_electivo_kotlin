package com.cee.model

import jakarta.persistence.Persistence
import org.flywaydb.core.Flyway
import org.junit.Test
import kotlin.test.assertNotNull
import java.time.LocalDate

class EntityPersistenceTest {
    @Test
    fun `entities persist and query`() {
        val dsUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        val flyway = Flyway.configure().dataSource(dsUrl, "sa", "").load()
        flyway.migrate()

        val emf = Persistence.createEntityManagerFactory("test")
        val em = emf.createEntityManager()
        em.transaction.begin()

        val admin = Admin("a1", "Admin", "admin@example.com")
        em.persist(admin)
        val votacion = Votacion(
            id = "v1",
            titulo = "Titulo",
            descripcion = "Desc",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now().plusDays(1),
            estado = "ABIERTA",
            admin = admin
        )
        em.persist(votacion)
        val opcion = Opcion(descripcion = "Opcion", votacion = votacion)
        em.persist(opcion)
        val simple = Simple("u1", "User", "user@example.com")
        em.persist(simple)
        val voto = Voto(fechaVoto = LocalDate.now(), opcion = opcion, usuario = simple)
        em.persist(voto)
        em.transaction.commit()

        val found = em.find(Voto::class.java, voto.id)
        assertNotNull(found)
    }
}
