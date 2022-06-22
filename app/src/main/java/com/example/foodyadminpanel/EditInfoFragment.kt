package com.example.foodyadminpanel

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodyadminpanel.databinding.FragmentEditInfoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar


class EditInfoFragment : Fragment() {
    private var imageUri: Uri? = null
    private var _binding: FragmentEditInfoBinding? = null
    private val binding get() = _binding!!
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        val resultCode = activityResult.resultCode
        val data = activityResult.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data != null && data.data != null) {
                    imageUri = data.data
                    binding.imageView.setImageURI(data.data)
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
    private val addedPrev = ArrayList<Uri>()
    private val getContent2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        val resultCode = activityResult.resultCode
        val data = activityResult.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data != null && data.data != null) {
                    addedPrev.add(data.data!!)
                    previewAdapter?.arrayList?.add(data.data?.toString()!!)
                    // if string starts with "content://" then upload
                    previewAdapter?.notifyDataSetChanged()

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


    private val viewModel: EditInfoViewModel by viewModels()
    private var cuisineAdapter: CuisineAdapter? = null
    private var previewAdapter: PreviewPhotoAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditInfoBinding.inflate(inflater, container, false)
        val restaurant = EditInfoFragmentArgs.fromBundle(requireArguments()).restaurant
        binding.restaurant = restaurant
        cuisineAdapter = CuisineAdapter(restaurant.cuisines!!, CuisineAdapter.CuisineRemoveCallBack {
            cuisineAdapter?.arrayList?.removeAt(it)
            cuisineAdapter?.notifyDataSetChanged()
        })

        previewAdapter =
            PreviewPhotoAdapter(restaurant.foodPreviewList!!, PreviewPhotoAdapter.DeletePreviewItem(onClick = object : (String, Int) -> Unit {
                override fun invoke(imageUrl: String, position: Int) {
                    if (imageUrl.startsWith("http")) {
                        viewModel.removePreviewImage(imageUrl)
                    }
                    restaurant.foodPreviewList?.removeAt(position)
                    previewAdapter?.notifyDataSetChanged()
                }

            }))
        viewModel.removePreviewImageState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    Toast.makeText(requireContext(), "Image's been removed successfully", Toast.LENGTH_SHORT).show()
                }
                is DataState.Error -> {
                    Snackbar.make(requireView(), it.exception.message!!, Snackbar.LENGTH_LONG).show()
                }
                else -> {

                }
            }

        }
        binding.photosAdd.setOnClickListener {
            ImagePicker.with(this).createIntent {
                getContent2.launch(it)
            }
        }
        binding.cuisineRecyclerView.adapter = cuisineAdapter
        binding.photosRecyclerView.adapter = previewAdapter
        binding.addCuisine.setOnClickListener {
            if ((!binding.newCuisineEditText.text.isNullOrEmpty())
                && (binding.newCuisineEditText.text.toString() !in cuisineAdapter?.arrayList!!)
            ) {
                cuisineAdapter?.arrayList?.add(binding.newCuisineEditText.text.toString())
                cuisineAdapter?.notifyDataSetChanged()
                binding.newCuisineEditText.text.clear()

            }
        }

        binding.saveFab.setOnClickListener {
            val name = binding.nameInputEditText.text.toString()
            val loc = binding.locationInputEditText.text.toString()
            val workingHours = binding.workingHoursInputEditText.text.toString()
            val cuisines = cuisineAdapter?.arrayList
            val imagesToUpload = previewAdapter?.arrayList?.filter {
                it.startsWith("content://")
            }
            viewModel.updateInfo(name, loc, workingHours, imageUri, cuisines, imagesToUpload)
        }
        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {

                }
                is DataState.Success -> {
                    when (it.data) {
                        0 -> {
                            Toast.makeText(requireContext(), "Updated successfully", Toast.LENGTH_LONG).show()

                        }
                        else -> {
                            Toast.makeText(requireContext(), "Updated with an Error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is DataState.Error -> {
                    Log.d(tag, "onCreateView: ${it.exception.message!!}")
                }
                else -> {

                }
            }

        }
        binding.imageView.setOnClickListener {
            ImagePicker.with(this).createIntent {
                getContent.launch(it)
            }
        }
        binding.newCuisineCheckBox.setOnCheckedChangeListener { _, b ->
            when (b) {
                true -> {
                    binding.newCuisineEditText.visibility = View.VISIBLE
                    binding.addCuisine.visibility = View.VISIBLE

                }
                false -> {
                    binding.newCuisineEditText.visibility = View.INVISIBLE
                    binding.addCuisine.visibility = View.INVISIBLE

                }
            }
        }

        return binding.root
    }

}
