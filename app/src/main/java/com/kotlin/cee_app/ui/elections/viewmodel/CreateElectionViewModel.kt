package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.VotacionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class CreateElectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)
    private val _opciones = MutableStateFlow<List<String>>(emptyList())
    val opciones: StateFlow<List<String>> = _opciones

    fun agregarOpcion(text: String) {
        _opciones.value = _opciones.value + text
    }

    fun eliminarOpcion(text: String) {
        _opciones.value = _opciones.value - text
    }
    private var editId: String? = null

    fun cargar(id: String) {
        editId = id
        viewModelScope.launch {
            repo.obtenerVotacion(id)?.let {
                _titulo.value = it.titulo
                _descripcion.value = it.descripcion
            }
            repo.opcionesDeVotacion(id).collect { list ->
                _opciones.value = list.map { it.descripcion }
            }
        }
    }

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion

    fun guardar(titulo: String, descripcion: String) {
        viewModelScope.launch {
            val id = editId ?: UUID.randomUUID().toString()
            val votacion = VotacionEntity(
                id = id,
                titulo = titulo,
                descripcion = descripcion,
                fechaInicio = LocalDate.now(),
                fechaFin = LocalDate.now(),
                estado = "Abierta",
                adminId = SessionManager.currentUserId
            )
            if (editId == null) {
                repo.insertarVotacion(votacion)
            } else {
                repo.actualizarVotacion(votacion)
                repo.eliminarOpciones(id)
            }
            _opciones.value.forEach {
                repo.insertarOpcion(OpcionEntity(descripcion = it, votacionId = id))
            }
        }
    }
}
