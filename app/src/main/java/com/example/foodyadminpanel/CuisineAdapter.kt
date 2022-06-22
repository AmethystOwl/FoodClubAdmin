package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.ItemCuisineBinding

class CuisineAdapter(val arrayList: ArrayList<String>, private val removeCallBack: CuisineRemoveCallBack) :
    RecyclerView.Adapter<CuisineAdapter.ViewHolder>() {
    private var oldPos: Int = -1

    class ViewHolder(private val binding: ItemCuisineBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(ItemCuisineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

        fun bind(cuisine: String, cuisineRemoveCallBack: CuisineRemoveCallBack, position: Int) {
            binding.cuisine = cuisine
            binding.onRemoveCallback = cuisineRemoveCallBack
            binding.position = position
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position], removeCallBack, position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class CuisineRemoveCallBack(private val callback: (position: Int) -> Unit) {
        fun onRemoveClick(position: Int) = callback(position)
    }
}
