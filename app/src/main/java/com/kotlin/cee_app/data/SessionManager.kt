package com.kotlin.cee_app.data

/**
 * Simula una sesión de usuario simple.
 */
object SessionManager {
    var currentUserId: String = ""
    var currentUserRole: String = ""

    fun isAdmin(): Boolean = currentUserRole == "ADMIN"

    fun clear() {
        currentUserId = ""
        currentUserRole = ""
    }
}
