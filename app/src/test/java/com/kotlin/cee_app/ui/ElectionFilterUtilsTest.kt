package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import com.kotlin.cee_app.ui.elections.viewmodel.splitActiveUpcomingPast
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class ElectionFilterUtilsTest {
    @Test
    fun split_returns_active_upcoming_and_past() {
        val today = LocalDate.of(2024, 1, 15)
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), EstadoVotacion.ABIERTA, "a", null),
            VotacionEntity("v2", "t2", "d", LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 20), EstadoVotacion.ABIERTA, "a", null),
            VotacionEntity("v3", "t3", "d", LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 20), EstadoVotacion.CERRADA, "a", null)
        )

        val (active, upcoming, past) = splitActiveUpcomingPast(list, today)

        assertEquals(1, active.size)
        assertEquals("v1", active.first().id)
        assertEquals(1, upcoming.size)
        assertEquals("v2", upcoming.first().id)
        assertEquals(1, past.size)
        assertEquals("v3", past.first().id)
    }

    @Test
    fun closed_future_considered_upcoming() {
        val today = LocalDate.of(2024, 1, 15)
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.of(2024, 1, 20), LocalDate.of(2024, 1, 25), EstadoVotacion.CERRADA, "a", null)
        )

        val (active, upcoming, past) = splitActiveUpcomingPast(list, today)

        assertTrue(active.isEmpty())
        assertEquals(1, upcoming.size)
        assertEquals("v1", upcoming.first().id)
        assertTrue(past.isEmpty())
    }

    @Test
    fun closed_today_considered_active() {
        val today = LocalDate.of(2024, 1, 15)
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), EstadoVotacion.CERRADA, "a", null)
        )

        val (active, upcoming, past) = splitActiveUpcomingPast(list, today)

        assertEquals(1, active.size)
        assertEquals("v1", active.first().id)
        assertTrue(upcoming.isEmpty())
        assertTrue(past.isEmpty())
    }
}
