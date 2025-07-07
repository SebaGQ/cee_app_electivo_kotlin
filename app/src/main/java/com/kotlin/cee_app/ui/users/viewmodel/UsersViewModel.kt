package com.kotlin.cee_app.ui.users.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.kotlin.cee_app.data.UsuarioEntity

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository.getInstance(application)

    private val _usuarios = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val usuarios: StateFlow<List<UsuarioEntity>> = _usuarios

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
