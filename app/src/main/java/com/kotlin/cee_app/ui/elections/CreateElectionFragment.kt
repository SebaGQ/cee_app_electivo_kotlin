package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.kotlin.cee_app.ui.elections.viewmodel.CreateElectionViewModel
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import android.app.DatePickerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.widget.EditText
import androidx.activity.addCallback
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentCreateElectionBinding
import com.kotlin.cee_app.data.entity.EstadoVotacion

class CreateElectionFragment : Fragment() {

    private var _binding: FragmentCreateElectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateElectionViewModel by viewModels()
    private val args: CreateElectionFragmentArgs by navArgs()
    private var saved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!SessionManager.isAdmin()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.admin_only, Snackbar.LENGTH_SHORT).show()
        }
        _binding = FragmentCreateElectionBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!saved) {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.confirm_discard_changes)
                    .setPositiveButton(android.R.string.ok) { _, _ -> findNavController().navigateUp() }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            } else {
                findNavController().navigateUp()
            }
        }

        args.votacionId?.let { id ->
            viewModel.cargar(id)
        }

        viewModel.tituloLiveData.observe(viewLifecycleOwner) {
            binding.editTitle.setText(it)
        }
        viewModel.descripcionLiveData.observe(viewLifecycleOwner) {
            binding.editDescription.setText(it)
        }
        val formatter = DateTimeFormatter.ISO_DATE
        viewModel.fechaInicioLiveData.observe(viewLifecycleOwner) {
            binding.editStartDate.setText(it.format(formatter))
        }
        viewModel.fechaFinLiveData.observe(viewLifecycleOwner) {
            binding.editEndDate.setText(it.format(formatter))
        }
        viewModel.estadoLiveData.observe(viewLifecycleOwner) {
            binding.switchEstado.isChecked = it == EstadoVotacion.ABIERTA
        }
        viewModel.opcionesLiveData.observe(viewLifecycleOwner) { list ->
            binding.chipGroup.removeAllViews()
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
                    setOnClickListener {
                        showOptionDialog(text) { newText ->
                            viewModel.actualizarOpcion(text, newText)
                        }
                    }
                }
                binding.chipGroup.addView(chip)
            }
        }

        binding.buttonAddOption.setOnClickListener {
            showOptionDialog("") { text ->
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
                    setOnClickListener {
                        showOptionDialog(this.text.toString()) { newText ->
                            viewModel.actualizarOpcion(this.text.toString(), newText)
                        }
                    }
                }
                binding.chipGroup.addView(chip)
                viewModel.agregarOpcion(chip.text.toString())
            }
        }

        binding.editStartDate.setOnClickListener {
            val date = viewModel.fechaInicio.value
            DatePickerDialog(requireContext(), { _, y, m, d ->
                viewModel.setFechaInicio(LocalDate.of(y, m + 1, d))
            }, date.year, date.monthValue - 1, date.dayOfMonth).show()
        }

        binding.editEndDate.setOnClickListener {
            val date = viewModel.fechaFin.value
            DatePickerDialog(requireContext(), { _, y, m, d ->
                viewModel.setFechaFin(LocalDate.of(y, m + 1, d))
            }, date.year, date.monthValue - 1, date.dayOfMonth).show()
        }

        binding.switchEstado.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEstado(isChecked)
        }

        binding.fabSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val desc = binding.editDescription.text.toString()
            if (viewModel.opciones.value.size < 2) {
                Snackbar.make(binding.root, R.string.need_two_options, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.guardar(title, desc,
                onError = {
                    Snackbar.make(binding.root, R.string.invalid_dates, Snackbar.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Snackbar.make(binding.root, R.string.saved, Snackbar.LENGTH_SHORT).show()
                    saved = true
                    findNavController().navigateUp()
                }
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showOptionDialog(text: String, onSave: (String) -> Unit) {
        val input = EditText(requireContext()).apply { setText(text) }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.option_name)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ -> onSave(input.text.toString()) }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}

