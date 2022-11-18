package com.example.tubespkb.fragment.loginRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tubespkb.activity.MainActivity
import com.example.tubespkb.util.RegisterValidation
import com.example.tubespkb.util.Resource
import com.example.tubespkb.viewmodel.LoginViewModel
import com.example.tubespkb.R
import com.example.tubespkb.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener{
                val email = loginEditTextEmail.text.toString().trim()
                val password = loginEditTextPassword.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.toRegisterText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when(it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Intent(requireActivity(), MainActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                if(it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main){
                        binding.loginEditTextEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if(it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main){
                        binding.loginEditTextPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }
}