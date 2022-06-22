package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.ItemCategoryBinding

class MenuCategoryAdapter2(
    private val items: ArrayList<MenuCategory>,
    private val onMenuCategorySelected: OnMenuCategorySelected
) :
    RecyclerView.Adapter<MenuCategoryAdapter2.ViewHolder>() {
    private var oldPos = 0

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_category, parent, false)
                return ViewHolder(ItemCategoryBinding.bind(view))
            }
        }

        fun bind(category: MenuCategory, position: Int, onMenuCategorySelected: OnMenuCategorySelected) {
            binding.category = category
            binding.onCategorySelected = onMenuCategorySelected
            binding.position = position
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position, onMenuCategorySelected)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class OnMenuCategorySelected(private val callback: (MenuCategory, Int) -> Unit) {
        fun onSelect(category: MenuCategory, position: Int) = callback(category, position)
    }

    fun setCurrentSelected(position: Int) {
        if(oldPos == position) return
        if (position == -1) {
            items[oldPos].isSelected = false
            notifyItemChanged(oldPos)
            oldPos = position
        } else {
            items[position].isSelected = true
            if(oldPos != -1){
                items[oldPos].isSelected = false
                notifyItemChanged(oldPos)
            }
            notifyItemChanged(position)
            oldPos = position
        }

    }
}
