package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.ui.elections.viewmodel.splitActiveUpcomingPast
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class ElectionFilterUtilsTest {
    @Test
    fun split_returns_active_upcoming_and_past() {
        val today = LocalDate.of(2024, 1, 15)
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 20), "Abierta", "a"),
            VotacionEntity("v2", "t2", "d", LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 20), "Abierta", "a"),
            VotacionEntity("v3", "t3", "d", LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 20), "Cerrada", "a")
        )

        val (active, upcoming, past) = splitActiveUpcomingPast(list, today)

        assertEquals(1, active.size)
        assertEquals("v1", active.first().id)
        assertEquals(1, upcoming.size)
        assertEquals("v2", upcoming.first().id)
        assertEquals(1, past.size)
        assertEquals("v3", past.first().id)
    }
}
