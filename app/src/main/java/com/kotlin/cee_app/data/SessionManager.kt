package com.kotlin.cee_app.data

/**
 * Simula una sesión de usuario simple.
 */
object SessionManager {
    var currentUserId: String = "admin1"
    var currentUserRole: String = "ADMIN"

    fun isAdmin(): Boolean = currentUserRole == "ADMIN"
}
