package com.kotlin.cee_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
data class AdminEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val correo: String,
)
