package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.DialogVoteBinding
import com.kotlin.cee_app.ui.elections.viewmodel.VoteDetailViewModel

class VoteDialogFragment : DialogFragment() {

    private var _binding: DialogVoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VoteDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogVoteBinding.inflate(inflater, container, false)
        val id = requireArguments().getString(ARG_ID) ?: ""
        viewModel.cargar(id)

        viewModel.votacion.asLiveData().observe(viewLifecycleOwner) { v ->
            binding.textPregunta.text = v?.titulo ?: ""
        }

        viewModel.opciones.asLiveData().observe(viewLifecycleOwner) { list ->
            binding.radioGroup.removeAllViews()
            list.forEach { opcion ->
                val rb = RadioButton(requireContext())
                rb.text = opcion.descripcion
                rb.tag = opcion.id
                binding.radioGroup.addView(rb)
            }
        }

        binding.buttonAction.setOnClickListener {
            val checked = binding.radioGroup.checkedRadioButtonId
            if (binding.textConfirmation.visibility == View.GONE) {
                if (checked != View.NO_ID) {
                    val tag = binding.radioGroup.findViewById<RadioButton>(checked).tag as Long
                    viewModel.votar(tag,
                        onDuplicate = {
                            dismiss()
                        },
                        onSuccess = {
                            binding.radioGroup.visibility = View.GONE
                            binding.textConfirmation.visibility = View.VISIBLE
                            binding.buttonAction.setText(R.string.button_ok)
                        },
                    )
                }
            } else {
                dismiss()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "votacionId"
        fun newInstance(id: String): VoteDialogFragment {
            val frag = VoteDialogFragment()
            frag.arguments = bundleOf(ARG_ID to id)
            return frag
        }
    }
}
