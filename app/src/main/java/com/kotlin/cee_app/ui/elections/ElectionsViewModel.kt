package com.kotlin.cee_app.ui.elections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.repository.ElectionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la lista de votaciones.
 */
class ElectionsViewModel(private val repo: ElectionsRepository) : ViewModel() {

    private val _votaciones = MutableStateFlow<List<VotacionEntity>>(emptyList())
    val votaciones: StateFlow<List<VotacionEntity>> = _votaciones

    init {
        viewModelScope.launch {
            repo.getVotaciones().collect { _votaciones.value = it }
        }
    }

    suspend fun countVotos(votacionId: String) = repo.countVotos(votacionId)

    suspend fun countUsuarios() = repo.countUsuarios()

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = ElectionsRepository.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(repo) as T
        }
    }
}
