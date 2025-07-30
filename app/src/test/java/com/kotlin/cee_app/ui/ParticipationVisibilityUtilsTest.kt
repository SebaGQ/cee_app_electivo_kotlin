package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import com.kotlin.cee_app.ui.elections.viewmodel.shouldShowParticipation
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ParticipationVisibilityUtilsTest {
    @Test
    fun upcoming_voting_hides_participation() {
        val today = LocalDate.of(2024,1,1)
        val future = VotacionEntity(
            "v1",
            "title",
            "desc",
            LocalDate.of(2024,2,1),
            LocalDate.of(2024,2,5),
            EstadoVotacion.ABIERTA,
            "admin",
            null
        )
        assertFalse(shouldShowParticipation(future, today))
        val current = VotacionEntity(
            "v2",
            "title",
            "desc",
            LocalDate.of(2023,12,31),
            LocalDate.of(2024,1,5),
            EstadoVotacion.ABIERTA,
            "admin",
            null
        )
        assertTrue(shouldShowParticipation(current, today))
    }
}
