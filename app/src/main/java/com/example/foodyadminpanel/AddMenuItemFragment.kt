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
import com.example.foodyadminpanel.databinding.FragmentAddMenuItemBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar

class AddMenuItemFragment : Fragment() {
    private lateinit var adapter: MenuCategoryAdapter2

    private val viewModel: AddMenuItemViewModel by viewModels()
    private var _binding: FragmentAddMenuItemBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var selectedCategory: MenuCategory? = null
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
        _binding = FragmentAddMenuItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menuItemImageView.setOnClickListener {
            ImagePicker.with(this).createIntent {
                getContent.launch(it)
            }

        }
        val cats = AddMenuItemFragmentArgs.fromBundle(requireArguments()).categories
        val categories = ArrayList<MenuCategory>()
        for (cat in cats) {
            categories.add(cat)
        }
        categories[0].isSelected = true
        selectedCategory = categories[0]
        binding.newCategoryCheckBox.setOnCheckedChangeListener { _, b ->
            when (b) {
                true -> {
                    binding.newCategoryEditText.visibility = View.VISIBLE
                    adapter.setCurrentSelected(-1)
                    selectedCategory = null
                }
                false -> {
                    adapter.setCurrentSelected(0)
                    selectedCategory = categories[0]
                    binding.newCategoryEditText.visibility = View.INVISIBLE
                }
            }
        }

        adapter = MenuCategoryAdapter2(categories, MenuCategoryAdapter2.OnMenuCategorySelected(callback = object : (MenuCategory, Int) -> Unit {
            override fun invoke(menuCategory: MenuCategory, position: Int) {
                selectedCategory = menuCategory
                adapter.setCurrentSelected(position)
                binding.newCategoryCheckBox.isChecked = false
            }
        }))
        binding.categoriesRecyclerView.adapter = adapter

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
            if(imageUri == null){
                Snackbar.make(requireView(),"Please Select an image",Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.nameInputLayout.error = null
            binding.descriptionInputLayout.error = null
            binding.priceInputLayout.error = null
            val item = MenuItem2(null, null, name, desc, null, price.toDouble())
            if (binding.newCategoryCheckBox.isChecked &&
                binding.newCategoryEditText.text != null &&
                binding.newCategoryEditText.text.isNotEmpty()
            ) {
                for (cat in categories){
                    if(cat.categoryName == binding.newCategoryEditText.text.toString()){
                        viewModel.addMenuItem(item, cat, imageUri)
                        return@setOnClickListener
                    }
                }
                viewModel.addMenuItemWithCategory(binding.newCategoryEditText.text.toString(), item, imageUri)
            } else if (selectedCategory != null) {
                viewModel.addMenuItem(item, selectedCategory!!, imageUri)
            }
        }


        viewModel.addItemState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    enableViews(false)

                }
                is DataState.Success -> {
                    enableViews(true)
                    Toast.makeText(requireContext(), "Added.", Toast.LENGTH_SHORT).show()
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

    private fun enableViews(isEnabled: Boolean) {
        binding.updateButton.isEnabled = isEnabled
        binding.nameInputEditText.isEnabled = isEnabled
        binding.descriptionInputEditText.isEnabled = isEnabled
        binding.priceInputEditText.isEnabled = isEnabled
        binding.menuItemImageView.isClickable = isEnabled
        binding.categoriesRecyclerView.isEnabled = isEnabled
    }

}
