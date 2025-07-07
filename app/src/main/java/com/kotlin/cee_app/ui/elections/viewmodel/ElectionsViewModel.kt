package com.kotlin.cee_app.ui.elections.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionPercent
import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.data.SessionManager
import java.time.LocalDate
import com.kotlin.cee_app.ui.elections.viewmodel.splitActiveUpcomingPast
import com.kotlin.cee_app.ui.elections.viewmodel.toPercent
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

    private val _upcoming = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val upcoming: StateFlow<List<VotacionEntity>> = _upcoming

    private val _past = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val past: StateFlow<List<VotacionEntity>> = _past

    private val _progress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val progress: StateFlow<Map<String, Int>> = _progress

    private val _voted = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val voted: StateFlow<Map<String, Boolean>> = _voted


    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers

    private val _optionsPercent = MutableStateFlow<Map<String, List<OpcionPercent>>>(emptyMap())
    val optionsPercent: StateFlow<Map<String, List<OpcionPercent>>> = _optionsPercent

    private val _optionsCount = MutableStateFlow<Map<String, List<ConteoOpcion>>>(emptyMap())
    val optionsCount: StateFlow<Map<String, List<ConteoOpcion>>> = _optionsCount

    init {
        viewModelScope.launch {
            repo.votaciones.collect { list ->
                _votaciones.value = list
                val (act, upcomingList, pastList) = splitActiveUpcomingPast(list, LocalDate.now())
                _active.value = act
                _upcoming.value = upcomingList
                _past.value = pastList
                updateProgress(act + pastList)
                updateOptions(act + pastList)
                updateVoted(act)
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


    private suspend fun updateOptions(list: List<VotacionEntity>) {
        val percentMap = mutableMapOf<String, List<OpcionPercent>>()
        val countMap = mutableMapOf<String, List<ConteoOpcion>>()
        list.forEach { v ->
            val conteos = repo.resultados(v.id)
            countMap[v.id] = conteos
            percentMap[v.id] = toPercent(conteos)
        }
        _optionsPercent.value = percentMap
        _optionsCount.value = countMap
    }

    private suspend fun updateVoted(list: List<VotacionEntity>) {
        val map = mutableMapOf<String, Boolean>()
        list.forEach { v ->
            map[v.id] = repo.haVotado(v.id, SessionManager.currentUserId)
        }
        _voted.value = map
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
            val (act, upcomingList, pastList) = splitActiveUpcomingPast(list, LocalDate.now())
            _active.value = act
            _upcoming.value = upcomingList
            _past.value = pastList
            updateProgress(act + pastList)
            updateOptions(act + pastList)
            updateVoted(act)
        }
    }
}
