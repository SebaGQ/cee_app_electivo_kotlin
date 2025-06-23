package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.VotacionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)

    private val _votaciones = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val votaciones: StateFlow<List<VotacionEntity>> = _votaciones

    private val _active = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val active: StateFlow<List<VotacionEntity>> = _active

    private val _past = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val past: StateFlow<List<VotacionEntity>> = _past

    private val _progress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val progress: StateFlow<Map<String, Int>> = _progress

    private val _winners = MutableStateFlow<Map<String, String>>(emptyMap())
    val winners: StateFlow<Map<String, String>> = _winners

    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers

    init {
        viewModelScope.launch {
            repo.votaciones.collect { list ->
                _votaciones.value = list
                _active.value = list.filter { it.estado == "Abierta" }
                val pastList = list.filter { it.estado != "Abierta" }
                _past.value = pastList
                updateProgress(_active.value)
                updateWinners(pastList)
            }
        }
        viewModelScope.launch {
            _totalUsers.value = repo.totalUsuarios()
        }
    }

    private suspend fun updateProgress(list: List<VotacionEntity>) {
        val map = mutableMapOf<String, Int>()
        list.forEach { v ->
            map[v.id] = repo.contarVotos(v.id)
        }
        _progress.value = map
    }

    private suspend fun updateWinners(list: List<VotacionEntity>) {
        val map = mutableMapOf<String, String>()
        list.forEach { v ->
            val opcion = repo.opcionGanadora(v.id)
            opcion?.let { map[v.id] = it.descripcion }
        }
        _winners.value = map
    }

    fun eliminar(votacion: VotacionEntity) {
        viewModelScope.launch {
            repo.eliminarVotacion(votacion.id)
        }
    }
}
