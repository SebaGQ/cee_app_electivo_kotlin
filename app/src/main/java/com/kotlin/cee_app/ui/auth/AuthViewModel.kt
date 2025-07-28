package com.kotlin.cee_app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.repository.UserRepository
import com.kotlin.cee_app.data.entity.UsuarioEntity
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserRepository.getInstance(application)

    fun login(correo: String, password: String, onResult: (UsuarioEntity?) -> Unit) {
        viewModelScope.launch { onResult(repo.login(correo, password)) }
    }

    fun signUp(
        nombre: String,
        correo: String,
        password: String,
        isAdmin: Boolean,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val user = UsuarioEntity(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                correo = correo,
                password = password,
                rol = if (isAdmin) "ADMIN" else "USER"
            )
            repo.insertar(user)
            onComplete()
        }
    }
}
