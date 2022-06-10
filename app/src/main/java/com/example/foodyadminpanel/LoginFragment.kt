package com.example.foodyadminpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodyadminpanel.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {


    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.emailInputEditText.text.toString(),
                binding.passwordInputEditText.text.toString()
            )
        }



        viewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
                is DataState.Error -> {
                    Snackbar.make(requireView(),it.exception.message!!,Snackbar.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
        return binding.root
    }


}
