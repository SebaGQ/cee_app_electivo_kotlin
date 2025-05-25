package com.cee.model

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SIMPLE")
class Simple(
    id: String,
    nombre: String,
    correo: String
) : Usuario(id, nombre, correo, "SIMPLE")
