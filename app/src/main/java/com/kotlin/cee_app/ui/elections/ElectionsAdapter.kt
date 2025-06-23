package com.kotlin.cee_app.ui.elections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.data.VotacionEntity
import com.kotlin.cee_app.databinding.ItemElectionBinding

/**
 * Adaptador para la lista de votaciones.
 */
class ElectionsAdapter(
    private val onClick: (VotacionEntity) -> Unit
) : RecyclerView.Adapter<ElectionsAdapter.ElectionViewHolder>() {

    private var items: List<VotacionEntity> = emptyList()
    private var progress: Map<String, Int> = emptyMap()

    fun submitList(list: List<VotacionEntity>, progressMap: Map<String, Int>) {
        items = list
        progress = progressMap
        notifyDataSetChanged()
    }

    inner class ElectionViewHolder(private val binding: ItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VotacionEntity) {
            binding.textTitle.text = item.titulo
            binding.textEstado.text = item.estado
            val showProgress = item.estado == "Abierta"
            binding.progressVotes.visibility = if (showProgress) View.VISIBLE else View.GONE
            if (showProgress) {
                binding.progressVotes.progress = progress[item.id] ?: 0
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemElectionBinding.inflate(inflater, parent, false)
        return ElectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
