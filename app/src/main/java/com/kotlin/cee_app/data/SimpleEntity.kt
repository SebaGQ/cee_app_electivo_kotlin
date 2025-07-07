package com.kotlin.cee_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simples")
data class SimpleEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val correo: String,
)
