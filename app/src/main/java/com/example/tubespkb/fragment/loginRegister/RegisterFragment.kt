package com.example.tubespkb.fragment.loginRegister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tubespkb.R
import com.example.tubespkb.data.User
import com.example.tubespkb.viewmodel.RegisterViewModel
import com.example.tubespkb.databinding.FragmentRegisterBinding
import com.example.tubespkb.util.RegisterValidation
import com.example.tubespkb.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerButton.setOnClickListener {
                val user = User(
                    registerEditTextName.text.toString().trim(),
                    registerEditTextEmail.text.toString().trim()
                )
                val password = registerEditTextPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        binding.toLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when(it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Log.d("test", it.data.toString())
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if(it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main){
                        binding.registerEditTextEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if(it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main){
                        binding.registerEditTextPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }
}