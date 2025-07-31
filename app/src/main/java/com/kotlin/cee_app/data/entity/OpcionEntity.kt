package com.kotlin.cee_app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opciones")
data class OpcionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val descripcion: String,
    val votacionId: String,
)
