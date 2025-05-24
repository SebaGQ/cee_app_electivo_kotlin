package com.cee.model

import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "votaciones")
class Votacion(
    @Id
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate,
    val estado: String,
    @ManyToOne
    @JoinColumn(name = "admin_id")
    val admin: Admin,
    @OneToMany(mappedBy = "votacion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val opciones: MutableList<Opcion> = mutableListOf()
)
