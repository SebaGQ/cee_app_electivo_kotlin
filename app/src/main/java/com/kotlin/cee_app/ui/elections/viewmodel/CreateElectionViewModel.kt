package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.repository.ElectionRepository
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.entity.VotacionEntity
import com.kotlin.cee_app.data.entity.EstadoVotacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class CreateElectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)
    private val _opciones = MutableStateFlow<List<String>>(emptyList())
    val opciones: StateFlow<List<String>> = _opciones
    val opcionesLiveData = _opciones.asLiveData()

    private val _fechaInicio = MutableStateFlow(LocalDate.now())
    val fechaInicio: StateFlow<LocalDate> = _fechaInicio
    val fechaInicioLiveData = _fechaInicio.asLiveData()

    private val _fechaFin = MutableStateFlow(LocalDate.now())
    val fechaFin: StateFlow<LocalDate> = _fechaFin
    val fechaFinLiveData = _fechaFin.asLiveData()

    private val _estado = MutableStateFlow(EstadoVotacion.ABIERTA)
    val estado: StateFlow<EstadoVotacion> = _estado
    val estadoLiveData = _estado.asLiveData()

    fun setFechaInicio(date: LocalDate) { _fechaInicio.value = date }
    fun setFechaFin(date: LocalDate) { _fechaFin.value = date }
    fun setEstado(open: Boolean) { _estado.value = if (open) EstadoVotacion.ABIERTA else EstadoVotacion.CERRADA }

    fun agregarOpcion(text: String) {
        _opciones.value = _opciones.value + text
    }

    fun eliminarOpcion(text: String) {
        _opciones.value = _opciones.value - text
    }

    fun actualizarOpcion(old: String, new: String) {
        _opciones.value = _opciones.value.map { if (it == old) new else it }
    }
    private var editId: String? = null

    fun cargar(id: String) {
        editId = id
        viewModelScope.launch {
            repo.obtenerVotacion(id)?.let {
                _titulo.value = it.titulo
                _descripcion.value = it.descripcion
                _fechaInicio.value = it.fechaInicio
                _fechaFin.value = it.fechaFin
                _estado.value = it.estado
            }
            repo.opcionesDeVotacion(id).collect { list ->
                _opciones.value = list.map { it.descripcion }
            }
        }
    }

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo
    val tituloLiveData = _titulo.asLiveData()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion
    val descripcionLiveData = _descripcion.asLiveData()

    fun guardar(titulo: String, descripcion: String, onError: () -> Unit, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val id = editId ?: UUID.randomUUID().toString()
            if (_opciones.value.size < 2) {
                onError()
                return@launch
            }
            if (_fechaFin.value.isBefore(_fechaInicio.value)) {
                onError()
                return@launch
            }

            val votacion = VotacionEntity(
                id = id,
                titulo = titulo,
                descripcion = descripcion,
                fechaInicio = _fechaInicio.value,
                fechaFin = _fechaFin.value,
                estado = _estado.value,
                adminId = SessionManager.currentUserId,
                finalParticipantCount = null
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
            onSuccess()
        }
    }
}
