package com.kotlin.cee_app.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kotlin.cee_app.databinding.FragmentCreateUserBinding
import com.kotlin.cee_app.ui.users.viewmodel.CreateUserViewModel

class CreateUserFragment : Fragment() {

    private var _binding: FragmentCreateUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateUserViewModel by viewModels()
    private val args: CreateUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserBinding.inflate(inflater, container, false)

        args.userId?.let { viewModel.cargar(it) }

        viewModel.nombre.observe(viewLifecycleOwner) { binding.editNombre.setText(it) }
        viewModel.correo.observe(viewLifecycleOwner) { binding.editCorreo.setText(it) }
        viewModel.password.observe(viewLifecycleOwner) { binding.editPassword.setText(it) }
        viewModel.rol.observe(viewLifecycleOwner) { binding.editRol.setText(it) }

        binding.fabSaveUser.setOnClickListener {
            viewModel.guardar(
                binding.editNombre.text.toString(),
                binding.editCorreo.text.toString(),
                binding.editPassword.text.toString(),
                binding.editRol.text.toString()
            ) { findNavController().popBackStack() }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
