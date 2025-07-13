package com.kotlin.cee_app.ui.results.viewmodel

import com.kotlin.cee_app.data.DashboardItem
import com.kotlin.cee_app.data.VotacionEntity

fun computeDashboard(
    list: List<VotacionEntity>,
    totalUsuarios: Int,
    totalVotos: Int,
): List<DashboardItem> {
    val total = list.size
    val abiertas = list.count { it.estado == "Abierta" }
    val cerradas = total - abiertas
    val participacion = if (totalUsuarios * total == 0) 0 else totalVotos * 100 / (totalUsuarios * total)
    val promedio = if (total == 0) 0 else totalVotos / total
    return listOf(
        DashboardItem("Votaciones existentes", total),
        DashboardItem("Abiertas", abiertas),
        DashboardItem("Cerradas", cerradas),
        DashboardItem("Participación %", participacion),
        DashboardItem("Promedio votantes", promedio),
    )
}
