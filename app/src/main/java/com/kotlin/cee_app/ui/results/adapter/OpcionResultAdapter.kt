package com.kotlin.cee_app.ui.results.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.model.OpcionPercent

class OpcionResultAdapter : RecyclerView.Adapter<OpcionResultAdapter.Vh>() {

    private var data: List<OpcionPercent> = emptyList()

    fun submit(list: List<OpcionPercent>) {
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_option, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = data[position]
        holder.option.text = item.descripcion
        holder.percent.text = "${item.porcentaje}%"
    }

    override fun getItemCount(): Int = data.size

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val option: TextView = itemView.findViewById(R.id.textOption)
        val percent: TextView = itemView.findViewById(R.id.textPercent)
    }
}
