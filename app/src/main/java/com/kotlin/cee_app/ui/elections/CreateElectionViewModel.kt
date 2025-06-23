package com.kotlin.cee_app.ui.elections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.cee_app.data.ElectionRepository
import com.kotlin.cee_app.data.OpcionEntity
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.VotacionEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class CreateElectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ElectionRepository.getInstance(application)
    val opciones = mutableListOf<String>()

    fun guardar(titulo: String, descripcion: String) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            val votacion = VotacionEntity(
                id = id,
                titulo = titulo,
                descripcion = descripcion,
                fechaInicio = LocalDate.now(),
                fechaFin = LocalDate.now(),
                estado = "Abierta",
                adminId = SessionManager.currentUserId
            )
            repo.insertarVotacion(votacion)
            opciones.forEach {
                repo.insertarOpcion(OpcionEntity(descripcion = it, votacionId = id))
            }
        }
    }
}
