package com.kotlin.cee_app.ui.results.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.data.DashboardItem
import com.kotlin.cee_app.data.ElectionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ElectionRepository.getInstance(application)

    private val _datos = MutableStateFlow<List<ConteoOpcion>>(emptyList())
    val datos: StateFlow<List<ConteoOpcion>> = _datos

    private val _dashboard = MutableStateFlow<List<DashboardItem>>(emptyList())
    val dashboard: StateFlow<List<DashboardItem>> = _dashboard

    fun cargarResultados(votacionId: String) {
        viewModelScope.launch {
            _datos.value = repo.resultados(votacionId)
        }
    }

    fun cargarDashboard() {
        viewModelScope.launch {
            val list = repo.votaciones.first()
            _dashboard.value = computeDashboard(list)
        }
    }
}
