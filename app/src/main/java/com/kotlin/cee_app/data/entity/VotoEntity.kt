package com.kotlin.cee_app.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "votos",
    indices = [Index(value = ["votacionId", "usuarioId"], unique = true)]
)
data class VotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fechaVoto: LocalDate,
    val opcionId: Long,
    val usuarioId: String,
    val votacionId: String,
)
