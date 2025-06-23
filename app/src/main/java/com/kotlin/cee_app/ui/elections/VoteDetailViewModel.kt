package com.kotlin.cee_app.ui.elections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.VotoEntity
import com.kotlin.cee_app.repository.ElectionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel para el detalle de una votación.
 */
class VoteDetailViewModel(
    private val repository: ElectionsRepository,
    private val votacionId: String,
) : ViewModel() {

    private val _opciones = MutableStateFlow<List<OpcionEntity>>(emptyList())
    val opciones: StateFlow<List<OpcionEntity>> = _opciones

    init {
        viewModelScope.launch {
            repository.getOpciones(votacionId).collect { _opciones.value = it }
        }
    }

    fun votar(opcionId: Long, usuarioId: String) {
        viewModelScope.launch {
            val voto = VotoEntity(
                fechaVoto = LocalDate.now(),
                opcionId = opcionId,
                usuarioId = usuarioId,
            )
            repository.insertVoto(voto)
        }
    }

    class Factory(private val context: Context, private val votacionId: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = ElectionsRepository.getInstance(context)
            @Suppress("UNCHECKED_CAST")
            return VoteDetailViewModel(repo, votacionId) as T
        }
    }
}
