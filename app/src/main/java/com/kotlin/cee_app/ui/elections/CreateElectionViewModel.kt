package com.kotlin.cee_app.ui.elections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.repository.ElectionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

/**
 * ViewModel para la creación de nuevas votaciones.
 */
class CreateElectionViewModel(private val repository: ElectionsRepository) : ViewModel() {

    private val _saved = MutableStateFlow<Boolean?>(null)
    val saved: StateFlow<Boolean?> = _saved

    fun saveElection(title: String, description: String, start: LocalDate, options: List<String>, adminId: String) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            val votacion = VotacionEntity(
                id = id,
                titulo = title,
                descripcion = description,
                fechaInicio = start,
                fechaFin = start,
                estado = "Abierta",
                adminId = adminId,
            )
            repository.insertVotacion(votacion)
            options.forEach { desc ->
                repository.insertOpcion(OpcionEntity(descripcion = desc, votacionId = id))
            }
            _saved.value = true
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = ElectionsRepository.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return CreateElectionViewModel(repo) as T
        }
    }
}
