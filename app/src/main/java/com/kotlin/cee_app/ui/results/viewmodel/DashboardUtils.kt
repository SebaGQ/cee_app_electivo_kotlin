package com.kotlin.cee_app.ui.results.viewmodel

import com.kotlin.cee_app.data.model.DashboardItem
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.VotoEntity
import java.time.LocalDate

data class ExtendedDashboardItem(
    val label: String,
    val value: String,
    val percentage: Float? = null,
    val isPercentage: Boolean = false
)

data class DashboardData(
    val totalVotaciones: Int,
    val votacionesAbiertas: Int,
    val votacionesCerradas: Int,
    val totalUsuarios: Int,
    val totalVotos: Int,
    val porcentajeParticipacion: Float,
    val promedioParticipantes: Float,
    val votacionMasParticipada: String?,
    val votacionMenosParticipada: String?
)

fun computeExtendedDashboard(
    votaciones: List<VotacionEntity>,
    totalUsuarios: Int,
    votosConteo: Map<String, Int>
): DashboardData {
    val total = votaciones.size
    val hoy = LocalDate.now()

    val abiertas = votaciones.count {
        it.estado == "Abierta" &&
                hoy.isAfter(it.fechaInicio.minusDays(1)) &&
                hoy.isBefore(it.fechaFin.plusDays(1))
    }

    val cerradas = votaciones.count {
        it.estado == "Cerrada" || hoy.isAfter(it.fechaFin)
    }

    val totalVotos = votosConteo.values.sum()

    // Calcular porcentaje de participación
    val maxVotosPosibles = totalUsuarios * votaciones.size
    val porcentajeParticipacion = if (maxVotosPosibles > 0) {
        (totalVotos.toFloat() / maxVotosPosibles) * 100
    } else 0f

    // Calcular promedio de participantes por votación
    val promedioParticipantes = if (votaciones.isNotEmpty()) {
        totalVotos.toFloat() / votaciones.size
    } else 0f

    // Encontrar votación más y menos participada
    val votacionMasParticipada = votosConteo.maxByOrNull { it.value }?.let { entry ->
        votaciones.find { it.id == entry.key }?.titulo
    }

    val votacionMenosParticipada = votosConteo.filter { it.value > 0 }
        .minByOrNull { it.value }?.let { entry ->
            votaciones.find { it.id == entry.key }?.titulo
        }

    return DashboardData(
        totalVotaciones = total,
        votacionesAbiertas = abiertas,
        votacionesCerradas = cerradas,
        totalUsuarios = totalUsuarios,
        totalVotos = totalVotos,
        porcentajeParticipacion = porcentajeParticipacion,
        promedioParticipantes = promedioParticipantes,
        votacionMasParticipada = votacionMasParticipada,
        votacionMenosParticipada = votacionMenosParticipada
    )
}

fun dashboardDataToItems(data: DashboardData): List<ExtendedDashboardItem> {
    return listOf(
        ExtendedDashboardItem(
            label = "Total Votaciones",
            value = data.totalVotaciones.toString(),
            isPercentage = false
        ),
        ExtendedDashboardItem(
            label = "Votaciones Abiertas",
            value = data.votacionesAbiertas.toString(),
            isPercentage = false
        ),
        ExtendedDashboardItem(
            label = "Votaciones Cerradas",
            value = data.votacionesCerradas.toString(),
            isPercentage = false
        ),
        ExtendedDashboardItem(
            label = "Participación General",
            value = "%.1f%%".format(data.porcentajeParticipacion),
            percentage = data.porcentajeParticipacion,
            isPercentage = true
        ),
        ExtendedDashboardItem(
            label = "Promedio Participantes",
            value = "%.1f".format(data.promedioParticipantes),
            isPercentage = false
        ),
        ExtendedDashboardItem(
            label = "Total Usuarios Registrados",
            value = data.totalUsuarios.toString(),
            isPercentage = false
        ),
        ExtendedDashboardItem(
            label = "Total Votos Emitidos",
            value = data.totalVotos.toString(),
            isPercentage = false
        )
    ).also { list ->
        data.votacionMasParticipada?.let {
            list + ExtendedDashboardItem(
                label = "Más Participada",
                value = it,
                isPercentage = false
            )
        }
        data.votacionMenosParticipada?.let {
            list + ExtendedDashboardItem(
                label = "Menos Participada",
                value = it,
                isPercentage = false
            )
        }
    }
}