package com.kotlin.cee_app.ui.results.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.model.DashboardItem

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.Vh>() {

    private var data: List<DashboardItem> = emptyList()

    fun submit(list: List<DashboardItem>) {
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = data[position]
        holder.label.text = item.label
        holder.value.text = item.count.toString()
    }

    override fun getItemCount(): Int = data.size

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.textLabel)
        val value: TextView = itemView.findViewById(R.id.textValue)
    }
}
