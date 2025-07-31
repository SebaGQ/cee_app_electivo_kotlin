package com.kotlin.cee_app.ui.users.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.kotlin.cee_app.data.entity.UsuarioEntity

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository.getInstance(application)

    private val _usuarios = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val usuarios: StateFlow<List<UsuarioEntity>> = _usuarios
    val usuariosLiveData = _usuarios.asLiveData()

    init {
        viewModelScope.launch {
            repo.usuarios.collect { list ->
                _usuarios.value = list
            }
        }
    }

    fun eliminar(id: String) {
        viewModelScope.launch { repo.eliminar(id) }
    }
}
