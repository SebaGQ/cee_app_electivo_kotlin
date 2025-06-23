package com.kotlin.cee_app.ui.elections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    private val _votacion = MutableStateFlow<VotacionEntity?>(null)
    val votacion: StateFlow<VotacionEntity?> = _votacion

    fun cargar(votacionId: String) {
        viewModelScope.launch {
            _votacion.value = repo.obtenerVotacion(votacionId)
        }
        viewModelScope.launch {
            repo.opcionesDeVotacion(votacionId).collect {
                _opciones.value = it
            }
        }
    }

    fun votar(opcionId: Long) {
        viewModelScope.launch {
            repo.insertarVoto(
                VotoEntity(
                    fechaVoto = LocalDate.now(),
                    opcionId = opcionId,
                    usuarioId = SessionManager.currentUserId
                )
            )
        }
    }
}
