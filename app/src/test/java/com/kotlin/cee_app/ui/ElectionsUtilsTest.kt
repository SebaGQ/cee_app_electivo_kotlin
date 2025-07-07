package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.ui.elections.viewmodel.splitActiveFuture
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ElectionsUtilsTest {
    @Test
    fun split_active_and_future_lists() {
        val today = LocalDate.now()
        val list = listOf(
            VotacionEntity("v1", "t", "d", today.minusDays(1), today.plusDays(1), "Abierta", "a1"),
            VotacionEntity("v2", "t", "d", today.plusDays(2), today.plusDays(3), "Abierta", "a1"),
            VotacionEntity("v3", "t", "d", today.minusDays(5), today.minusDays(1), "Cerrada", "a1")
        )
        val (active, future) = splitActiveFuture(list)
        assertEquals(1, active.size)
        assertEquals("v1", active.first().id)
        assertEquals(1, future.size)
        assertEquals("v2", future.first().id)
    }
}
