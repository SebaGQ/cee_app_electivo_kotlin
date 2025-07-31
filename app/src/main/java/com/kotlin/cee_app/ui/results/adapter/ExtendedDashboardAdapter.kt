package com.kotlin.cee_app.ui.results.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.ui.results.viewmodel.ExtendedDashboardItem

class ExtendedDashboardAdapter : RecyclerView.Adapter<ExtendedDashboardAdapter.ViewHolder>() {

    private var items: List<ExtendedDashboardItem> = emptyList()

    fun submitList(list: List<ExtendedDashboardItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isPercentage) TYPE_PERCENTAGE else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = when (viewType) {
            TYPE_PERCENTAGE -> R.layout.item_dashboard_percentage
            else -> R.layout.item_dashboard_extended
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View, private val viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private val textLabel: TextView = itemView.findViewById(R.id.textLabel)
        private val textValue: TextView = itemView.findViewById(R.id.textValue)
        private val progressBar: ProgressBar? = if (viewType == TYPE_PERCENTAGE) {
            itemView.findViewById(R.id.progressBar)
        } else null

        fun bind(item: ExtendedDashboardItem) {
            textLabel.text = item.label
            textValue.text = item.value

            if (viewType == TYPE_PERCENTAGE && progressBar != null) {
                progressBar.progress = item.percentage?.toInt() ?: 0
            }
        }
    }

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_PERCENTAGE = 1
    }
}