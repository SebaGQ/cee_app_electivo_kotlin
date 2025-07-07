package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.data.isActive
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VotacionEntityTest {
    @Test
    fun isActive_true_when_today_between_dates() {
        val v = VotacionEntity(
            "id", "t", "d",
            LocalDate.of(2024,1,1),
            LocalDate.of(2024,1,10),
            "Abierta", "a1"
        )
        assertTrue(v.isActive(LocalDate.of(2024,1,5)))
    }

    @Test
    fun isActive_false_out_of_range_or_closed() {
        val v = VotacionEntity(
            "id", "t", "d",
            LocalDate.of(2024,1,1),
            LocalDate.of(2024,1,10),
            "Abierta", "a1"
        )
        assertFalse(v.isActive(LocalDate.of(2023,12,31)))
        assertFalse(v.isActive(LocalDate.of(2024,1,11)))
        val closed = v.copy(estado = "Cerrada")
        assertFalse(closed.isActive(LocalDate.of(2024,1,5)))
    }
}
