package com.cee.model

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("ADMIN")
class Admin(
    id: String,
    nombre: String,
    correo: String
) : Usuario(id, nombre, correo, "ADMIN")
