package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.VotoEntity
import com.kotlin.cee_app.data.VotacionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class VoteDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)

    private val _opciones = MutableStateFlow<List<OpcionEntity>>(emptyList())
    val opciones: StateFlow<List<OpcionEntity>> = _opciones
    val opcionesLiveData = _opciones.asLiveData()

    private val _votacion = MutableStateFlow<VotacionEntity?>(null)
    val votacion: StateFlow<VotacionEntity?> = _votacion
    val votacionLiveData = _votacion.asLiveData()

    private val _yaVoto = MutableStateFlow(false)
    val yaVoto: StateFlow<Boolean> = _yaVoto
    val yaVotoLiveData = _yaVoto.asLiveData()

    private val _opcionVotada = MutableStateFlow<Long?>(null)
    val opcionVotada: StateFlow<Long?> = _opcionVotada
    val opcionVotadaLiveData = _opcionVotada.asLiveData()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    val isLoadingLiveData = _isLoading.asLiveData()

    private val _isVoting = MutableStateFlow(false)
    val isVoting: StateFlow<Boolean> = _isVoting
    val isVotingLiveData = _isVoting.asLiveData()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    val errorLiveData = _error.asLiveData()

    fun cargar(votacionId: String) {
        if (_isLoading.value) return // Evitar cargas duplicadas

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                _votacion.value = repo.obtenerVotacion(votacionId)
                _yaVoto.value = repo.haVotado(votacionId, SessionManager.currentUserId)
                if (_yaVoto.value) {
                    _opcionVotada.value = repo.opcionVotadaPorUsuario(
                        votacionId,
                        SessionManager.currentUserId
                    )
                } else {
                    _opcionVotada.value = null
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar la votación: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }

        viewModelScope.launch {
            try {
                repo.opcionesDeVotacion(votacionId).collect {
                    _opciones.value = it
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar las opciones: ${e.message}"
            }
        }
    }

    fun votar(
        opcionId: Long,
        onDuplicate: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val votacionId = _votacion.value?.id
        if (votacionId == null) {
            onError("Error: No se pudo obtener la información de la votación")
            return
        }

        if (_isVoting.value) return // Evitar votos duplicados

        _isVoting.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val votoEntity = VotoEntity(
                    fechaVoto = LocalDate.now(),
                    opcionId = opcionId,
                    usuarioId = SessionManager.currentUserId,
                    votacionId = votacionId,
                )

                val ok = repo.insertarVoto(votoEntity)

                if (ok) {
                    _yaVoto.value = true
                    _opcionVotada.value = opcionId
                    onSuccess()
                } else {
                    onDuplicate()
                }
            } catch (e: Exception) {
                val errorMessage = "Error al enviar el voto: ${e.message}"
                _error.value = errorMessage
                onError(errorMessage)
            } finally {
                _isVoting.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetVotingState() {
        _isVoting.value = false
    }

    // Método de conveniencia para verificar si se puede votar
    fun canVote(): Boolean {
        return !_yaVoto.value && !_isVoting.value && _votacion.value != null
    }

    // Método para obtener la opción seleccionada por ID
    fun getOpcionById(opcionId: Long): OpcionEntity? {
        return _opciones.value.find { it.id == opcionId }
    }

    // Método para verificar si la votación está activa
    fun isVotacionActiva(): Boolean {
        val votacion = _votacion.value ?: return false
        val hoy = LocalDate.now()

        return when {
            votacion.fechaInicio != null && hoy.isBefore(votacion.fechaInicio) -> false
            votacion.fechaFin != null && hoy.isAfter(votacion.fechaFin) -> false
            else -> true
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Limpiar recursos si es necesario
    }
}