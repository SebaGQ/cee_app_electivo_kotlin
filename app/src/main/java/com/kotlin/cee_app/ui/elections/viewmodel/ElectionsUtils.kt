package com.kotlin.cee_app.ui.elections.viewmodel

import com.kotlin.cee_app.data.VotacionEntity
import java.time.LocalDate

fun splitActiveFuture(list: List<VotacionEntity>): Pair<List<VotacionEntity>, List<VotacionEntity>> {
    val today = LocalDate.now()
    val active = list.filter {
        it.estado == "Abierta" &&
            !today.isBefore(it.fechaInicio) && !today.isAfter(it.fechaFin)
    }
    val future = list.filter { it.estado == "Abierta" && today.isBefore(it.fechaInicio) }
    return Pair(active, future)
}
