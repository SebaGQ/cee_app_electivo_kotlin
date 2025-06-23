package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.asLiveData
import com.kotlin.cee_app.ui.elections.viewmodel.CreateElectionViewModel
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentCreateElectionBinding

class CreateElectionFragment : Fragment() {

    private var _binding: FragmentCreateElectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateElectionViewModel by viewModels()
    private val args: CreateElectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!SessionManager.isAdmin()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Solo admin", Snackbar.LENGTH_SHORT).show()
        }
        _binding = FragmentCreateElectionBinding.inflate(inflater, container, false)

        args.votacionId?.let { id ->
            viewModel.cargar(id)
        }

        viewModel.titulo.asLiveData().observe(viewLifecycleOwner) {
            binding.editTitle.setText(it)
        }
        viewModel.descripcion.asLiveData().observe(viewLifecycleOwner) {
            binding.editDescription.setText(it)
        }
        viewModel.opciones.asLiveData().observe(viewLifecycleOwner) { list ->
            binding.chipGroup.removeAllViews()
            var counter = 1
            list.forEach { text ->
                val chip = Chip(requireContext()).apply {
                    this.text = text
                    isCloseIconVisible = true
                    chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.background_gray)
                    )
                    chipStrokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.primary_blue)
                    )
                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        viewModel.eliminarOpcion(text.toString())
                    }
                }
                binding.chipGroup.addView(chip)
                counter++
            }
        }

        var counter = 1
        binding.buttonAddOption.setOnClickListener {
            val chip = Chip(requireContext()).apply {
                text = "Opcion ${counter++}"
                isCloseIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.background_gray)
                )
                chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.primary_blue)
                )
                setOnCloseIconClickListener {
                    binding.chipGroup.removeView(this)
                    viewModel.eliminarOpcion(text.toString())
                }
            }
            binding.chipGroup.addView(chip)
            viewModel.agregarOpcion(chip.text.toString())
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

