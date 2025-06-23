package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentCreateElectionBinding

class CreateElectionFragment : Fragment() {

    private var _binding: FragmentCreateElectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateElectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!SessionManager.isAdmin()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Solo admin", Snackbar.LENGTH_SHORT).show()
        }
        _binding = FragmentCreateElectionBinding.inflate(inflater, container, false)

        var counter = 1
        binding.buttonAddOption.setOnClickListener {
            val chip = Chip(requireContext()).apply {
                text = "Opcion ${counter++}"
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    binding.chipGroup.removeView(this)
                    viewModel.opciones.remove(text.toString())
                }
            }
            binding.chipGroup.addView(chip)
            viewModel.opciones.add(chip.text.toString())
        }

        binding.fabSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val desc = binding.editDescription.text.toString()
            viewModel.guardar(title, desc)
            Snackbar.make(binding.root, "Guardado", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
