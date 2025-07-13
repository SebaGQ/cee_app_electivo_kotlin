package com.kotlin.cee_app.ui.results.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.data.DashboardItem
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.UserRepository
import com.kotlin.cee_app.data.VotacionEntity
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
                val votacionesParticipacion = votaciones.map { votacion ->
                    val votos = votosConteo[votacion.id] ?: 0
                    val porcentaje = if (totalUsuarios > 0) {
                        (votos.toFloat() / totalUsuarios) * 100
                    } else 0f

                    VotacionConParticipacion(
                        votacion = votacion,
                        totalVotos = votos,
                        porcentajeParticipacion = porcentaje
                    )
                }.sortedByDescending { it.porcentajeParticipacion }

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