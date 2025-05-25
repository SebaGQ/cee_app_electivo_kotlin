package com.cee.model

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rol")
abstract class Usuario(
    @Id
    val id: String,
    val nombre: String,
    val correo: String,
    @Column(insertable = false, updatable = false)
    val rol: String? = null
)
