package com.kotlin.cee_app.data.entity

enum class EstadoVotacion(val label: String) {
    ABIERTA("Abierta"),
    CERRADA("Cerrada"),
    FINALIZADA("Finalizada");

    companion object {
        fun fromString(value: String): EstadoVotacion =
            values().firstOrNull { it.label.equals(value, ignoreCase = true) } ?: ABIERTA
    }
}
