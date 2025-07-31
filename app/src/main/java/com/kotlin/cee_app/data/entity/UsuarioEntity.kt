package com.kotlin.cee_app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val correo: String,
    val password: String,
    val rol: String,
)
