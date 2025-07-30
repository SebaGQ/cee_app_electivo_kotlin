package com.kotlin.cee_app.ui.elections.viewmodel

import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import java.time.LocalDate

internal fun splitActiveUpcoming(
    list: List<VotacionEntity>,
    today: LocalDate = LocalDate.now(),
): Pair<List<VotacionEntity>, List<VotacionEntity>> {
    val active = list.filter { v ->
        v.estado == EstadoVotacion.ABIERTA &&
            !today.isBefore(v.fechaInicio) &&
            !today.isAfter(v.fechaFin)
    }
    val upcoming = list.filter { v ->
        v.estado == EstadoVotacion.ABIERTA &&
            today.isBefore(v.fechaInicio)
    }
    return active to upcoming
}

internal fun splitActiveUpcomingPast(
    list: List<VotacionEntity>,
    today: LocalDate = LocalDate.now(),
): Triple<List<VotacionEntity>, List<VotacionEntity>, List<VotacionEntity>> {
    val (active, upcoming) = splitActiveUpcoming(list, today)
    val past = list.filterNot { it in active || it in upcoming }
    return Triple(active, upcoming, past)
}
