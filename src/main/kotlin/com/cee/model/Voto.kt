package com.cee.model

import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "votos")
class Voto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fechaVoto: LocalDate,
    @ManyToOne
    @JoinColumn(name = "opcion_id")
    val opcion: Opcion,
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    val usuario: Simple
)
