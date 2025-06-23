package com.kotlin.cee_app.ui.elections.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.VotacionEntity

class VotacionAdapter(
    private val onClick: (VotacionEntity) -> Unit
) : RecyclerView.Adapter<VotacionAdapter.Vh>() {

    private var data: List<VotacionEntity> = emptyList()
    private var progress: Map<String, Int> = emptyMap()
    private var totalUsers: Int = 1

    fun submit(list: List<VotacionEntity>, progressMap: Map<String, Int>, total: Int) {
        data = list
        progress = progressMap
        totalUsers = if (total == 0) 1 else total
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_votacion, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = data[position]
        holder.title.text = item.titulo
        holder.estado.text = item.estado
        val count = progress[item.id] ?: 0
        if (item.estado == "Abierta") {
            holder.progress.visibility = View.VISIBLE
            holder.progress.max = totalUsers
            holder.progress.progress = count
        } else {
            holder.progress.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = data.size

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val estado: TextView = itemView.findViewById(R.id.textEstado)
        val progress: ProgressBar = itemView.findViewById(R.id.progressVotos)
    }
}
