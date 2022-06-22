package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.CategoryLayoutBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class MenuCategoryAdapter(
    options: FirestoreRecyclerOptions<MenuCategory>,
    private val fireStore: FirebaseFirestore,
    private val restaurantId: String,
    private val onMenuItemClickListener: MenuItemAdapter2.OnMenuItemClickListener,
) : FirestoreRecyclerAdapter<MenuCategory, MenuCategoryAdapter.ViewHolder>(options) {

    class ViewHolder(private val binding: CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.category_layout, parent, false)
                return ViewHolder(CategoryLayoutBinding.bind(view))
            }
        }

        fun bind(
            category: MenuCategory,
            onMenuItemClickListener: MenuItemAdapter2.OnMenuItemClickListener,
            fireStore: FirebaseFirestore,
            restaurantId: String,
        ) {

            binding.categoryName = category.categoryName

            val query = fireStore.collection("restaurants")
                .document(restaurantId)
                .collection("MenuCategories")
                .document(category.id!!)
                .collection("MenuItems")
                .limit(10000)
            val options = FirestoreRecyclerOptions.Builder<MenuItem2>().setQuery(query, MenuItem2::class.java).build()
            val menuItemAdapter = MenuItemAdapter2(options, onMenuItemClickListener)
            binding.menuItemsRecyclerView.adapter = menuItemAdapter
            menuItemAdapter.startListening()
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MenuCategory) {
        model.id = snapshots.getSnapshot(position).id
        holder.bind(model, onMenuItemClickListener, fireStore, restaurantId)
    }

}
