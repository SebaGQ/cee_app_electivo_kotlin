package com.kotlin.cee_app.data

/**
 * Clase simple que simula una sesión de usuario.
 * Para propósitos de demostración devuelve un usuario fijo.
 */
object SessionManager {
    // Usuario en sesión; por simplicidad se usa uno por defecto de rol ADMIN.
    val currentUser: UsuarioEntity = UsuarioEntity(
        id = "u1",
        nombre = "Admin Demo",
        correo = "admin@demo.com",
        rol = "ADMIN",
    )
}
