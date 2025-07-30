package com.kotlin.cee_app.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kotlin.cee_app.R
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

        val rolesDisplay = listOf(
            getString(R.string.role_user),
            getString(R.string.role_admin)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, rolesDisplay)
        (binding.editRol as MaterialAutoCompleteTextView).setAdapter(adapter)

        binding.editRol.setOnItemClickListener { parent, _, position, _ ->
            val value = if (position == 0) "SIMPLE" else "ADMIN"
            viewModel.setRol(value)
        }

        args.userId?.let { viewModel.cargar(it) }

        viewModel.nombre.observe(viewLifecycleOwner) { binding.editNombre.setText(it) }
        viewModel.correo.observe(viewLifecycleOwner) { binding.editCorreo.setText(it) }
        viewModel.password.observe(viewLifecycleOwner) { binding.editPassword.setText(it) }
        viewModel.rol.observe(viewLifecycleOwner) { role ->
            val display = if (role == "ADMIN") getString(R.string.role_admin) else getString(R.string.role_user)
            (binding.editRol as MaterialAutoCompleteTextView).setText(display, false)
        }

        binding.fabSaveUser.setOnClickListener {
            viewModel.guardar(
                binding.editNombre.text.toString(),
                binding.editCorreo.text.toString(),
                binding.editPassword.text.toString(),
                viewModel.rol.value ?: "SIMPLE"
            ) { findNavController().popBackStack() }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
