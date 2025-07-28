package com.kotlin.cee_app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.MainActivity
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.LoginFragmentBinding
import com.kotlin.cee_app.ui.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.buttonLogin.setOnClickListener {
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.login(correo, password) { success ->
                    if (success) {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Snackbar.make(binding.root, R.string.invalid_credentials, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signUp)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
