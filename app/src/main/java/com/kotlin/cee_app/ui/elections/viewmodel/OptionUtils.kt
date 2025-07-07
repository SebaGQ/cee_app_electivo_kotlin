package com.kotlin.cee_app.ui.elections.viewmodel

import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.data.OpcionPercent

internal fun toPercent(list: List<ConteoOpcion>): List<OpcionPercent> {
    val total = list.sumOf { it.total }
    return list.map { co ->
        val pct = if (total == 0) 0 else (co.total * 100 / total)
        OpcionPercent(co.descripcion, pct)
    }
}
