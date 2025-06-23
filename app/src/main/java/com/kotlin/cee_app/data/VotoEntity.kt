package com.kotlin.cee_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "votos")
data class VotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fechaVoto: LocalDate,
    val opcionId: Long,
    val usuarioId: String,
)
