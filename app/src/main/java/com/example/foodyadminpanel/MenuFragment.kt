package com.example.foodyadminpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodyadminpanel.databinding.FragmentMenuBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuFragment : Fragment() {

    private var adapter: MenuCategoryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        val fireStore = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid!!
        val query = fireStore.collection("restaurants")
            .document(uid)
            .collection("MenuCategories")
            .limit(10000)
        val options = FirestoreRecyclerOptions.Builder<MenuCategory>().setQuery(query, MenuCategory::class.java).build()
        val adapter = MenuCategoryAdapter(
            options,
            fireStore,
            uid,
            MenuItemAdapter2.OnMenuItemClickListener(onClickListener = object : (MenuItem2) -> Unit {
                override fun invoke(menuItem: MenuItem2) {
                    findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToMenuItemFragment(menuItem))
                }
            })
        )
        binding.menuRecycler.adapter = adapter
        adapter.startListening()
        binding.addButton.setOnClickListener {
            fireStore.collection("restaurants")
                .document(uid)
                .collection("MenuCategories").get().addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            val docs = it.result.documents
                            val categories = ArrayList<MenuCategory>()
                            for (doc in docs) {
                                val cat = doc.toObject(MenuCategory::class.java)!!
                                cat.id = doc.id
                                categories.add(cat)
                            }
                            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAddMenuItemFragment(categories.toTypedArray()))
                        }
                    }
                }


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.stopListening()
    }
}
