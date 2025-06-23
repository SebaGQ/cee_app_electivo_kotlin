package com.kotlin.cee_app.ui.elections

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

    private val _progress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val progress: StateFlow<Map<String, Int>> = _progress

    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers

    init {
        viewModelScope.launch {
            repo.votaciones.collect {
                _votaciones.value = it
                updateProgress(it)
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
}
