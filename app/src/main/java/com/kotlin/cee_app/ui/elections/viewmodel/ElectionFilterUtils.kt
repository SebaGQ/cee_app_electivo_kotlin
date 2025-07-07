package com.kotlin.cee_app.ui.elections.viewmodel

import com.kotlin.cee_app.data.VotacionEntity
import java.time.LocalDate

internal fun splitActiveUpcoming(list: List<VotacionEntity>, today: LocalDate = LocalDate.now()): Pair<List<VotacionEntity>, List<VotacionEntity>> {
    val active = list.filter { v ->
        v.estado.equals("Abierta", ignoreCase = true) &&
                !today.isBefore(v.fechaInicio) &&
                !today.isAfter(v.fechaFin)
    }
    val upcoming = list.filter { v ->
        v.estado.equals("Abierta", ignoreCase = true) &&
                today.isBefore(v.fechaInicio)
    }
    return active to upcoming
}
