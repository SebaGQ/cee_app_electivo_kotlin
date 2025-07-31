package com.kotlin.cee_app.ui

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
//import com.kotlin.cee_app.ui.results.viewmodel.computeDashboard
import com.kotlin.cee_app.ui.results.viewmodel.DashboardData
import com.kotlin.cee_app.ui.results.viewmodel.dashboardDataToItems
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class DashboardUtilsTest {
//    @Test
//    fun compute_counts_correct() {
//        val list = listOf(
//            VotacionEntity("v1", "t1", "d", LocalDate.now(), LocalDate.now(), EstadoVotacion.ABIERTA, "a1", null),
//            VotacionEntity("v2", "t2", "d", LocalDate.now(), LocalDate.now(), EstadoVotacion.CERRADA, "a1", null)
//        )
//        val result = computeDashboard(list)
//        assertEquals(3, result.size)
//        assertEquals(2, result[0].count)
//        assertEquals(1, result[1].count)
//        assertEquals(1, result[2].count)
//    }

    @Test
    fun dashboard_items_include_optional_metrics() {
        val data = DashboardData(
            totalVotaciones = 1,
            votacionesAbiertas = 1,
            votacionesCerradas = 0,
            totalUsuarios = 10,
            totalVotos = 8,
            porcentajeParticipacion = 80f,
            promedioParticipantes = 8f,
            votacionMasParticipada = "V1",
            votacionMenosParticipada = "V2"
        )

        val items = dashboardDataToItems(data)

        assertEquals(9, items.size)
        assertEquals("Más Participada", items[7].label)
        assertEquals("V1", items[7].value)
        assertEquals("Menos Participada", items[8].label)
        assertEquals("V2", items[8].value)
    }
}
