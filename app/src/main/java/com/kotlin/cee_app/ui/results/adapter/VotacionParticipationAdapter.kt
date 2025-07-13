package com.kotlin.cee_app.ui.results.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.ui.results.viewmodel.VotacionConParticipacion
import java.time.format.DateTimeFormatter

class VotacionParticipationAdapter : RecyclerView.Adapter<VotacionParticipationAdapter.ViewHolder>() {

    private var items: List<VotacionConParticipacion> = emptyList()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun submitList(list: List<VotacionConParticipacion>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_votacion_participation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.textVotacionTitle)
        private val textDates: TextView = itemView.findViewById(R.id.textVotacionDates)
        private val textVotes: TextView = itemView.findViewById(R.id.textTotalVotes)
        private val textPercentage: TextView = itemView.findViewById(R.id.textParticipationPercentage)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressParticipation)
        private val textStatus: TextView = itemView.findViewById(R.id.textStatus)

        fun bind(item: VotacionConParticipacion) {
            val votacion = item.votacion

            textTitle.text = votacion.titulo
            textDates.text = "${votacion.fechaInicio.format(dateFormatter)} - ${votacion.fechaFin.format(dateFormatter)}"
            textVotes.text = "${item.totalVotos} votos"
            textPercentage.text = "%.1f%% participación".format(item.porcentajeParticipacion)
            progressBar.progress = item.porcentajeParticipacion.toInt()

            textStatus.text = votacion.estado
            val statusColor = if (votacion.estado == "Abierta") {
                itemView.context.getColor(android.R.color.holo_green_dark)
            } else {
                itemView.context.getColor(android.R.color.holo_red_dark)
            }
            textStatus.setTextColor(statusColor)
        }
    }
}