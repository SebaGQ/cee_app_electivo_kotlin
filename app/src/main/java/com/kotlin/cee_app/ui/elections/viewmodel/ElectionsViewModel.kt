package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionPercent
import com.kotlin.cee_app.data.VotacionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)

    private val _votaciones = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val votaciones: StateFlow<List<VotacionEntity>> = _votaciones

    private val _active = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val active: StateFlow<List<VotacionEntity>> = _active

    private val _future = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val future: StateFlow<List<VotacionEntity>> = _future

    private val _past = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val past: StateFlow<List<VotacionEntity>> = _past

    private val _progress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val progress: StateFlow<Map<String, Int>> = _progress

    private val _winners = MutableStateFlow<Map<String, String>>(emptyMap())
    val winners: StateFlow<Map<String, String>> = _winners

    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers

    private val _optionsPercent = MutableStateFlow<Map<String, List<OpcionPercent>>>(emptyMap())
    val optionsPercent: StateFlow<Map<String, List<OpcionPercent>>> = _optionsPercent


    init {
        viewModelScope.launch {
            repo.votaciones.collect { list ->
                _votaciones.value = list
                val (act, fut) = splitActiveFuture(list)
                _active.value = act
                _future.value = fut
                val pastList = list.filter { it.estado != "Abierta" }
                _past.value = pastList
                updateProgress(act)
                updateWinners(pastList)
                updateOptions(list)
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

    private suspend fun updateOptions(list: List<VotacionEntity>) {
        val map = mutableMapOf<String, List<OpcionPercent>>()
        list.forEach { v ->
            val conteos = repo.resultados(v.id)
            val total = conteos.sumOf { it.total }
            val opciones = conteos.map {
                val pct = if (total == 0) 0 else (it.total * 100 / total)
                OpcionPercent(it.descripcion, pct)
            }
            map[v.id] = opciones
        }
        _optionsPercent.value = map
    }

    fun eliminar(votacion: VotacionEntity) {
        viewModelScope.launch {
            repo.eliminarVotacion(votacion.id)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val list = repo.votaciones.first()
            _votaciones.value = list
            val (act, fut) = splitActiveFuture(list)
            _active.value = act
            _future.value = fut
            val pastList = list.filter { it.estado != "Abierta" }
            _past.value = pastList
            updateProgress(act)
            updateWinners(pastList)
            updateOptions(list)
        }
    }
}
