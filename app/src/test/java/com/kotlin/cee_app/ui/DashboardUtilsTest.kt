package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import com.kotlin.cee_app.ui.results.viewmodel.computeDashboard
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class DashboardUtilsTest {
    @Test
    fun compute_counts_correct() {
        val list = listOf(
            VotacionEntity("v1", "t1", "d", LocalDate.now(), LocalDate.now(), EstadoVotacion.ABIERTA, "a1", null),
            VotacionEntity("v2", "t2", "d", LocalDate.now(), LocalDate.now(), EstadoVotacion.CERRADA, "a1", null)
        )
        val result = computeDashboard(list)
        assertEquals(3, result.size)
        assertEquals(2, result[0].count)
        assertEquals(1, result[1].count)
        assertEquals(1, result[2].count)
    }
}
