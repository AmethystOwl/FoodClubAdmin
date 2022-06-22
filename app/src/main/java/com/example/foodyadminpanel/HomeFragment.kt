package com.example.foodyadminpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodyadminpanel.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var restaurant: Restaurant? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.getRestaurantInfo()

        viewModel.restaurantInfo.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    restaurant = it.data
                    binding.restaurant = restaurant
                }
                else -> {}
            }
        }
        binding.ordersCardView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOrdersFragment())
        }
        binding.menuCardView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuFragment())
        }
        binding.infoCardView.setOnClickListener {
            if (restaurant != null) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditInfoFragment(restaurant!!))
            }
        }
        return binding.root


    }


}
