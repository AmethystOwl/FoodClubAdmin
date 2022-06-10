package com.example.foodyadminpanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodyadminpanel.databinding.UserOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersAdapter(private var arrayList: ArrayList<Order>,
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    class ViewHolder(private val binding: UserOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(UserOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

        fun bind(order: Order) {
            val items = ArrayList<MenuItem>()
            val menuItemAdapter = MenuItemAdapter(items)
            binding.rv2.adapter = menuItemAdapter
            CoroutineScope(Dispatchers.IO).launch {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(order.userId!!)
                    .get()
                    .addOnCompleteListener {
                        when {
                            it.isSuccessful -> {
                                val user = it.result.toObject(User::class.java)
                                binding.order = order
                                binding.user = user
                                for (menuItemPair in order.categoryMealPair!!) {
                                    for (entry in menuItemPair.entries) {
                                        if (entry.key != "quantity") {
                                            FirebaseFirestore.getInstance()
                                                .collection("restaurants")
                                                .document(order.restaurantId!!)
                                                .collection("MenuCategories")
                                                .document(entry.key)
                                                .collection("MenuItems")
                                                .document(entry.value.toString())
                                                .get()
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        val menuItem = it.result.toObject(MenuItem::class.java)!!
                                                        menuItem.quantity = menuItemPair["quantity"].toString().toLong()
                                                        menuItem.total = menuItem.quantity!! * menuItem.price!!
                                                        menuItemAdapter.addItem(menuItem)
                                                    }
                                                }
                                        }

                                    }
                                }
                                binding.executePendingBindings()
                            }
                        }
                    }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount() = arrayList.size

    class OnMenuItemClickListener(private val onClickListener: (String,String) -> Unit) {
        fun onClick(parentId:String, menuItemId: String) = onClickListener(parentId,menuItemId)
    }
}
