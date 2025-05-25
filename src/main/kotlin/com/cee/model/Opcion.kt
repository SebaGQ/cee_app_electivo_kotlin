package com.cee.model

import jakarta.persistence.*

@Entity
@Table(name = "opciones")
class Opcion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val descripcion: String,
    @ManyToOne
    @JoinColumn(name = "votacion_id")
    val votacion: Votacion,
    @OneToMany(mappedBy = "opcion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val votos: MutableList<Voto> = mutableListOf()
)
