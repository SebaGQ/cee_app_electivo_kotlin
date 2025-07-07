package com.kotlin.cee_app.ui.results.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ConteoOpcion
import com.kotlin.cee_app.data.ElectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ElectionRepository.getInstance(application)

    private val _datos = MutableStateFlow<List<ConteoOpcion>>(emptyList())
    val datos: StateFlow<List<ConteoOpcion>> = _datos

    fun cargar(votacionId: String) {
        viewModelScope.launch {
            _datos.value = repo.resultados(votacionId)
        }
    }
}
