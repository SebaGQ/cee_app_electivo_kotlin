package com.kotlin.cee_app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import com.kotlin.cee_app.data.entity.EstadoVotacion

@Entity(tableName = "votaciones")
data class VotacionEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val estado: EstadoVotacion,
    val adminId: String,
    val finalParticipantCount: Int? = null,
    val cerrada: Boolean = false,
)
