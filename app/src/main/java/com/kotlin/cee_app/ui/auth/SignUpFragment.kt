package com.kotlin.cee_app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.buttonCreate.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                binding.editCorreo.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signUp(
                    nombre,
                    correo,
                    password,
                    binding.switchAdmin.isChecked
                ) {
                    Snackbar.make(binding.root, R.string.account_created, Snackbar.LENGTH_SHORT).show()
                    findNavController().popBackStack()
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
