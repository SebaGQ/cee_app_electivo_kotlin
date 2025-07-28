package com.kotlin.cee_app.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.entity.UsuarioEntity
import com.kotlin.cee_app.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository.getInstance(application)

    fun login(correo: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val usuario = repo.login(correo, password)
            if (usuario != null) {
                SessionManager.currentUserId = usuario.id
                SessionManager.currentUserRole = usuario.rol
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun signUp(nombre: String, correo: String, password: String, admin: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            val rol = if (admin) "ADMIN" else "USER"
            val usuario = UsuarioEntity(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                correo = correo,
                password = password,
                rol = rol
            )
            repo.insertar(usuario)
            onComplete()
        }
    }
}
