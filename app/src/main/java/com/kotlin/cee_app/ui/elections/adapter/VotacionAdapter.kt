package com.kotlin.cee_app.ui.elections.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.PopupMenu
import android.widget.LinearLayout
import android.content.res.ColorStateList
import java.time.LocalDate
import com.kotlin.cee_app.data.OpcionPercent
import com.kotlin.cee_app.data.ConteoOpcion
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.VotacionEntity

class VotacionAdapter(
    private val onClick: (VotacionEntity) -> Unit,
    private val onEdit: (VotacionEntity) -> Unit,
    private val onDelete: (VotacionEntity) -> Unit,
) : RecyclerView.Adapter<VotacionAdapter.Vh>() {

    private var data: List<VotacionEntity> = emptyList()
    private var progress: Map<String, Int> = emptyMap()
    private var options: Map<String, List<OpcionPercent>> = emptyMap()
    private var counts: Map<String, List<ConteoOpcion>> = emptyMap()
    private var totalUsers: Int = 1
    private var winners: Map<String, String> = emptyMap()
    private var voted: Map<String, Boolean> = emptyMap()
    private var votedOptions: Map<String, String?> = emptyMap()
    private val expanded: MutableSet<String> = mutableSetOf()

    private fun isActive(v: VotacionEntity): Boolean {
        val today = LocalDate.now()
        return v.estado.equals("Abierta", ignoreCase = true) &&
            !today.isBefore(v.fechaInicio) &&
            !today.isAfter(v.fechaFin)
    }

    fun submit(
        list: List<VotacionEntity>,
        progressMap: Map<String, Int>,
        optionsMap: Map<String, List<OpcionPercent>>,
        countsMap: Map<String, List<ConteoOpcion>>,
        total: Int,
        winnersMap: Map<String, String> = emptyMap(),
        votedMap: Map<String, Boolean> = emptyMap(),
        votedOptionMap: Map<String, String?> = emptyMap()
    ) {
        data = list
        progress = progressMap
        options = optionsMap
        counts = countsMap
        totalUsers = if (total == 0) 1 else total
        winners = winnersMap
        voted = votedMap
        votedOptions = votedOptionMap
        expanded.clear()
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
        holder.estado.setTextColor(
            if (item.estado == "Abierta") holder.itemView.context.getColor(R.color.primary_blue)
            else holder.itemView.context.getColor(R.color.black)
        )
        val count = progress[item.id] ?: 0
        val percent = if (totalUsers == 0) 0 else count * 100 / totalUsers
        holder.progressText.text = "$percent%"
        if (item.estado == "Abierta") {
            holder.progress.visibility = View.VISIBLE
            holder.progress.max = totalUsers
            holder.progress.progress = count
            holder.winner.visibility = View.GONE
        } else {
            holder.progress.visibility = View.GONE
            val w = winners[item.id]
            if (w != null) {
                holder.winner.visibility = View.VISIBLE
                holder.winner.text = holder.itemView.context.getString(
                    R.string.winner_label, w
                )
            } else {
                holder.winner.visibility = View.GONE
            }
        }

        holder.optionsContainer.removeAllViews()
        val opts = options[item.id] ?: emptyList()
        val countsList = counts[item.id] ?: emptyList()
        val already = voted[item.id] == true
        val votedDesc = votedOptions[item.id]
        opts.forEachIndexed { index, op ->
            val view = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_result_option, holder.optionsContainer, false)
            view.findViewById<TextView>(R.id.textOption).text = op.descripcion
            view.findViewById<TextView>(R.id.textPercent).text = "${op.porcentaje}%"
            val votesView = view.findViewById<TextView>(R.id.textVotes)
            val icon = view.findViewById<ImageView>(R.id.iconChecked)
            if (already) {
                val count = countsList.getOrNull(index)?.total ?: 0
                votesView.visibility = View.VISIBLE
                votesView.text = holder.itemView.context.getString(R.string.votes_count, count)
                if (votedDesc != null && votedDesc == op.descripcion) {
                    icon.visibility = View.VISIBLE
                } else {
                    icon.visibility = View.GONE
                }
            } else {
                votesView.visibility = View.GONE
                icon.visibility = View.GONE
            }
            holder.optionsContainer.addView(view)
        }

        val isExpanded = expanded.contains(item.id)
        holder.itemView.setOnClickListener {
            if (isExpanded) expanded.remove(item.id) else expanded.add(item.id)
            notifyItemChanged(position)
        }

        holder.buttonVote.visibility = if (isActive(item)) View.VISIBLE else View.GONE
        holder.buttonVote.isEnabled = !already
        if (already) {
            holder.buttonVote.backgroundTintList =
                ColorStateList.valueOf(holder.itemView.context.getColor(R.color.disabled_gray))
        } else {
            holder.buttonVote.backgroundTintList = null
        }
        holder.buttonVote.setOnClickListener { onClick(item) }

        holder.expandable.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expand_less
            else R.drawable.ic_expand_more
        )


        holder.menu.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.inflate(R.menu.menu_votacion_item)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> { onEdit(item); true }
                    R.id.action_delete -> { onDelete(item); true }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = data.size

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val estado: TextView = itemView.findViewById(R.id.textEstado)
        val progress: ProgressBar = itemView.findViewById(R.id.progressVotos)
        val progressText: TextView = itemView.findViewById(R.id.textProgress)
        val winner: TextView = itemView.findViewById(R.id.textWinner)
        val optionsContainer: LinearLayout = itemView.findViewById(R.id.layoutOptions)
        val menu: ImageView = itemView.findViewById(R.id.iconInfo)
        val expandIcon: ImageView = itemView.findViewById(R.id.iconExpand)
        val expandable: LinearLayout = itemView.findViewById(R.id.layoutExpandable)
        val buttonVote: View = itemView.findViewById(R.id.buttonVotar)
    }
}

