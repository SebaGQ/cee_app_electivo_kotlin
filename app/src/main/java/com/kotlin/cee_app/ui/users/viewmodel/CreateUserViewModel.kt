package com.kotlin.cee_app.ui.users.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.util.Patterns
import com.kotlin.cee_app.data.repository.UserRepository
import com.kotlin.cee_app.data.entity.UsuarioEntity
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CreateUserViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository.getInstance(application)

    private var editId: String? = null

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asLiveData()

    private val _correo = MutableStateFlow("")
    val correo = _correo.asLiveData()

    private val _password = MutableStateFlow("")
    val password = _password.asLiveData()

    private val _rol = MutableStateFlow("SIMPLE")
    val rol = _rol.asLiveData()

    fun setRol(r: String) { _rol.value = r }

    fun cargar(id: String) {
        editId = id
        viewModelScope.launch {
            repo.obtener(id)?.let {
                _nombre.value = it.nombre
                _correo.value = it.correo
                _password.value = it.password
                _rol.value = it.rol
            }
        }
    }

    fun guardar(
        nombre: String,
        correo: String,
        password: String,
        rol: String,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (nombre.isBlank() || correo.isBlank() || password.isBlank()) {
                onError()
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                onError()
                return@launch
            }
            val id = editId ?: UUID.randomUUID().toString()
            val usuario = UsuarioEntity(
                id = id,
                nombre = nombre,
                correo = correo,
                password = password,
                rol = rol
            )
            if (editId == null) repo.insertar(usuario) else repo.actualizar(usuario)
            onSuccess()
        }
    }
}
