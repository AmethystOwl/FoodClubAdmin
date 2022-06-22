package com.example.foodyadminpanel

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodyadminpanel.databinding.FragmentMenuItemBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar

class MenuItemFragment : Fragment() {


    private val viewModel : MenuItemViewModel by viewModels()
    private var _binding: FragmentMenuItemBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        val resultCode = activityResult.resultCode
        val data = activityResult.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data != null && data.data != null) {
                    imageUri = data.data
                    binding.menuItemImageView.setImageURI(data.data)
                }

            }
            ImagePicker.RESULT_ERROR -> {
                imageUri = null
                Snackbar.make(requireView(), "An error has occurred : $resultCode", Snackbar.LENGTH_LONG).show()
            }
            else -> {
                imageUri = null
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = MenuItemFragmentArgs.fromBundle(requireArguments()).menuItem
        binding.menuItem = item
        binding.menuItemImageView.setOnClickListener {
            ImagePicker.with(this).createIntent {
                getContent.launch(it)
            }

        }
        binding.updateButton.setOnClickListener {
            val name = binding.nameInputEditText.text?.toString()
            val desc = binding.descriptionInputEditText.text?.toString()
            val price = binding.priceInputEditText.text?.toString()

            if (name == null) {
                binding.nameInputLayout.error = "Name field cannot be empty"
                return@setOnClickListener
            }
            if (desc == null) {
                binding.descriptionInputLayout.error = "Description field cannot be empty"
                return@setOnClickListener
            }
            if (price == null) {
                binding.priceInputLayout.error = "Price field cannot be empty"
                return@setOnClickListener
            }
            if (price.toFloat() <= 0.0) {
                binding.priceInputLayout.error = "price cannot be less than or equal to zero"
                return@setOnClickListener
            }
            binding.nameInputLayout.error = null
            binding.descriptionInputLayout.error = null
            binding.priceInputLayout.error = null
            val newItem = MenuItem2(item.id, item.parentId, name, desc, item.imageUrl, price.toDouble())

            viewModel.updateMenuItem(newItem, imageUri)
            viewModel.updateState.observe(viewLifecycleOwner) {
                when (it) {
                    is DataState.Loading -> {
                        enableViews(false)

                    }
                    is DataState.Success -> {
                        enableViews(true)
                        Toast.makeText(requireContext(), "Updated.", Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Error -> {
                        enableViews(true)
                        Toast.makeText(requireContext(), it.exception.message!!, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        enableViews(true)
                    }
                }
            }
        }
    }

    private fun enableViews(isEnabled:Boolean) {
        binding.updateButton.isEnabled = isEnabled
        binding.nameInputEditText.isEnabled = isEnabled
        binding.descriptionInputEditText.isEnabled = isEnabled
        binding.priceInputEditText.isEnabled = isEnabled
        binding.menuItemImageView.isClickable = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
