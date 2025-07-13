package com.kotlin.cee_app.ui.results.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.model.ConteoOpcion
import com.kotlin.cee_app.data.model.DashboardItem
import com.kotlin.cee_app.data.repository.ElectionRepository
import com.kotlin.cee_app.data.repository.UserRepository
import com.kotlin.cee_app.data.entity.VotacionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {
    private val electionRepo = ElectionRepository.getInstance(application)
    private val userRepo = UserRepository.getInstance(application)

    private val _datos = MutableStateFlow<List<ConteoOpcion>>(emptyList())
    val datos: StateFlow<List<ConteoOpcion>> = _datos
    val datosLiveData = _datos.asLiveData()

    private val _dashboardItems = MutableStateFlow<List<ExtendedDashboardItem>>(emptyList())
    val dashboardItems: StateFlow<List<ExtendedDashboardItem>> = _dashboardItems
    val dashboardItemsLiveData = _dashboardItems.asLiveData()

    private val _dashboardData = MutableStateFlow<DashboardData?>(null)
    val dashboardData: StateFlow<DashboardData?> = _dashboardData
    val dashboardDataLiveData = _dashboardData.asLiveData()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    val isLoadingLiveData = _isLoading.asLiveData()

    private val _votacionesConParticipacion = MutableStateFlow<List<VotacionConParticipacion>>(emptyList())
    val votacionesConParticipacion: StateFlow<List<VotacionConParticipacion>> = _votacionesConParticipacion
    val votacionesConParticipacionLiveData = _votacionesConParticipacion.asLiveData()

    fun cargarResultados(votacionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _datos.value = electionRepo.resultados(votacionId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarDashboardCompleto() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Obtener todas las votaciones
                val votaciones = electionRepo.votaciones.first()

                // Obtener total de usuarios
                val totalUsuarios = electionRepo.totalUsuarios()

                // Obtener conteo de votos por votación (método optimizado)
                val votosConteo = electionRepo.obtenerVotosConteosPorVotacion()

                // Computar dashboard data
                val dashboardData = computeExtendedDashboard(
                    votaciones = votaciones,
                    totalUsuarios = totalUsuarios,
                    votosConteo = votosConteo
                )

                _dashboardData.value = dashboardData
                _dashboardItems.value = dashboardDataToItems(dashboardData)

                // Preparar lista de votaciones con su participación
                val votacionesParticipacion = computeParticipacionPorVotacion(
                    votaciones = votaciones,
                    votosConteo = votosConteo,
                    totalUsuarios = totalUsuarios
                )

                _votacionesConParticipacion.value = votacionesParticipacion

            } catch (e: Exception) {
                // Manejar error
                _dashboardItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class VotacionConParticipacion(
    val votacion: VotacionEntity,
    val totalVotos: Int,
    val porcentajeParticipacion: Float
)