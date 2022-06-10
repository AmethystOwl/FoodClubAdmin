package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.MenuItemBinding


class MenuItemAdapter(private val items: ArrayList<MenuItem>)
    : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.menu_item, parent, false)
                return ViewHolder(MenuItemBinding.bind(view))
            }
        }

        fun bind(menuItem: MenuItem) {
            binding.item = menuItem
            binding.executePendingBindings()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(menuItem: MenuItem) {
        items.add(menuItem)
        notifyDataSetChanged()
    }

}
