package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.MenuItem2Binding

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MenuItemAdapter2(
    options: FirestoreRecyclerOptions<MenuItem2>,
    private val onMenuItemClickListener: OnMenuItemClickListener) :
    FirestoreRecyclerAdapter<MenuItem2, MenuItemAdapter2.ViewHolder>(options) {
    class ViewHolder(val binding: MenuItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MenuItem2, onMenuItemClickListener: OnMenuItemClickListener) {
            binding.menuItem = model
            binding.onMenuItemClickListener = onMenuItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(MenuItem2Binding.bind(LayoutInflater.from(parent.context).inflate(R.layout.menu_item2, parent, false)))
            }
        }
    }

    class OnMenuItemClickListener(private val onClickListener: (MenuItem2) -> Unit) {
        fun onClick(menuItem:MenuItem2) = onClickListener(menuItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MenuItem2) {
        model.id = snapshots.getSnapshot(position).id
        model.parentId = snapshots.getSnapshot(position).reference.parent.parent?.id!!
        holder.bind(model, onMenuItemClickListener)
    }


}
