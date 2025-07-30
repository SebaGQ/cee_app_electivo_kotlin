package com.kotlin.cee_app.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import android.widget.ArrayAdapter
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

        args.userId?.let { viewModel.cargar(it) }

        viewModel.nombre.observe(viewLifecycleOwner) { binding.editNombre.setText(it) }
        viewModel.correo.observe(viewLifecycleOwner) { binding.editCorreo.setText(it) }
        viewModel.password.observe(viewLifecycleOwner) { binding.editPassword.setText(it) }

        val roles = resources.getStringArray(R.array.role_display_values).toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, roles)
        binding.editRol.setAdapter(adapter)

        viewModel.rol.observe(viewLifecycleOwner) { rol ->
            binding.editRol.setText(
                if (rol == "ADMIN") getString(R.string.role_admin) else getString(R.string.role_user),
                false
            )
        }

        binding.editRol.setOnItemClickListener { _, _, position, _ ->
            viewModel.setRol(if (position == 1) "ADMIN" else "SIMPLE")
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
