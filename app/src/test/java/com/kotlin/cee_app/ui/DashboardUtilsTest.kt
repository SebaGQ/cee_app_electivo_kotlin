package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.ui.results.viewmodel.computeDashboard
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class DashboardUtilsTest {
    @Test
    fun compute_counts_correct() {
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.now(), LocalDate.now(), "Abierta", "a1"),
            VotacionEntity("v2", "t2", "d", LocalDate.now(), LocalDate.now(), "Cerrada", "a1")
        )
        val result = computeDashboard(list, 4, 3)
        assertEquals(5, result.size)
        assertEquals(2, result[0].count)
        assertEquals(1, result[1].count)
        assertEquals(1, result[2].count)
        assertEquals(37, result[3].count)
        assertEquals(1, result[4].count)
    }
}
