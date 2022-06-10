package com.example.foodyadminpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodyadminpanel.databinding.FragmentOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {


    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOrdersBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            FirebaseFirestore.getInstance()
                .collection("restaurants")
                .document(FirebaseAuth.getInstance().uid!!)
                .collection("orders")
                .get()
                .addOnCompleteListener {
                    val ar = it.result.toObjects(Order::class.java)
                    val arr = ArrayList<Order>()
                    for (i in ar) {
                        arr.add(i)
                    }
                    val adapter = OrdersAdapter(arr)
                    binding.rv.adapter = adapter

                }
        }


        return binding.root


    }


}
