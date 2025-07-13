package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.ui.results.viewmodel.computeParticipacionPorVotacion
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ParticipationUtilsTest {
    @Test
    fun finalized_voting_uses_final_count() {
        val v1 = VotacionEntity(
            id = "v1",
            titulo = "Final",
            descripcion = "",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "Finalizada",
            adminId = "a",
            finalParticipantCount = 5
        )
        val v2 = VotacionEntity(
            id = "v2",
            titulo = "Open",
            descripcion = "",
            fechaInicio = LocalDate.now(),
            fechaFin = LocalDate.now(),
            estado = "Abierta",
            adminId = "a",
            finalParticipantCount = null
        )

        val votos = mapOf(
            "v1" to 10,
            "v2" to 3
        )

        val result = computeParticipacionPorVotacion(listOf(v1, v2), votos, 20)
        val item1 = result.first { it.votacion.id == "v1" }
        val item2 = result.first { it.votacion.id == "v2" }

        assertEquals(5, item1.totalVotos)
        assertEquals(25f, item1.porcentajeParticipacion)
        assertEquals(3, item2.totalVotos)
        assertEquals(15f, item2.porcentajeParticipacion)
    }
}
