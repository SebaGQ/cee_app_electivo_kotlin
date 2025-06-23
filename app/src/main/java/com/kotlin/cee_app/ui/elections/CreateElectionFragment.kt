package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentCreateElectionBinding
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Formulario para crear una nueva votación.
 */
class CreateElectionFragment : Fragment() {

    private var _binding: FragmentCreateElectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateElectionViewModel> { CreateElectionViewModel.Factory(requireContext()) }
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateElectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isAdmin = SessionManager.currentUser.rol == "ADMIN"
        if (!isAdmin) {
            Snackbar.make(view, "Solo administradores", Snackbar.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        binding.buttonAddOption.setOnClickListener {
            addOptionField()
        }

        binding.editDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.addOnPositiveButtonClickListener { millis ->
                selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                binding.editDate.setText(selectedDate.toString())
            }
            picker.show(parentFragmentManager, "date")
        }

        binding.fabSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val desc = binding.editDescription.text.toString()
            val options = gatherOptions()
            val date = selectedDate ?: LocalDate.now()
            viewModel.saveElection(title, desc, date, options, SessionManager.currentUser.id)
            Snackbar.make(it, "Votación creada", Snackbar.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }

    private fun addOptionField() {
        val chip = Chip(requireContext()).apply {
            isCloseIconVisible = true
            text = "Option ${binding.chipGroup.childCount + 1}"
            setOnCloseIconClickListener { binding.chipGroup.removeView(this) }
        }
        binding.chipGroup.addView(chip)
    }

    private fun gatherOptions(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i) as Chip
            list.add(chip.text.toString())
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
