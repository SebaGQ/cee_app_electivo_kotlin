package com.kotlin.cee_app.ui.results.viewmodel

import com.kotlin.cee_app.data.DashboardItem
import com.kotlin.cee_app.data.VotacionEntity

fun computeDashboard(list: List<VotacionEntity>): List<DashboardItem> {
    val total = list.size
    val abiertas = list.count { it.estado == "Abierta" }
    val cerradas = total - abiertas
    return listOf(
        DashboardItem("Total votaciones", total),
        DashboardItem("Abiertas", abiertas),
        DashboardItem("Cerradas", cerradas)
    )
}
