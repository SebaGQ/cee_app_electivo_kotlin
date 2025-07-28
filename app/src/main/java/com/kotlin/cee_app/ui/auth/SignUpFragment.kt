package com.kotlin.cee_app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.SignUpFragmentBinding
import com.kotlin.cee_app.ui.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        binding.buttonCreate.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                binding.editCorreo.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signUp(nombre, correo, password, binding.switchAdmin.isChecked) {
                    Snackbar.make(binding.root, R.string.account_created, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUp_to_login)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
