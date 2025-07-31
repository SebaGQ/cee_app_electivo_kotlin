package com.kotlin.cee_app.ui.users.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.google.android.material.chip.Chip
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.entity.UsuarioEntity

class UserAdapter(
    private val onEdit: (UsuarioEntity) -> Unit,
    private val onDelete: (UsuarioEntity) -> Unit,
) : RecyclerView.Adapter<UserAdapter.Vh>() {

    private var data: List<UsuarioEntity> = emptyList()

    fun submit(list: List<UsuarioEntity>) {
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val item = data[position]
        holder.nombre.text = item.nombre
        holder.correo.text = item.correo
        holder.rol.text = when (item.rol) {
            "ADMIN" -> holder.itemView.context.getString(R.string.role_admin)
            else -> holder.itemView.context.getString(R.string.role_user)
        }

        holder.menu.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.inflate(R.menu.menu_user_item)
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
        val nombre: TextView = itemView.findViewById(R.id.textNombre)
        val correo: TextView = itemView.findViewById(R.id.textCorreo)
        val rol: Chip = itemView.findViewById(R.id.chipRol)
        val menu: ImageView = itemView.findViewById(R.id.iconMenu)
    }
}
