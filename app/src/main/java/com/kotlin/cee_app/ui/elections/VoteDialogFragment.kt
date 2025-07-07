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
import com.google.android.material.snackbar.Snackbar
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
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogVoteBinding.inflate(inflater, container, false)
        val votacionId = requireArguments().getString(ARG_VOTACION) ?: ""
        viewModel.cargar(votacionId)

        viewModel.votacion.asLiveData().observe(viewLifecycleOwner) { v ->
            binding.textPregunta.text = v?.titulo ?: ""
        }

        viewModel.opciones.asLiveData().observe(viewLifecycleOwner) { list ->
            binding.radioGroup.removeAllViews()
            list.forEach { opcion ->
                val rb = layoutInflater
                    .inflate(R.layout.item_vote_option, binding.radioGroup, false) as RadioButton
                rb.text = opcion.descripcion
                rb.tag = opcion.id
                binding.radioGroup.addView(rb)
            }
        }

        binding.buttonVote.setOnClickListener {
            val checked = binding.radioGroup.checkedRadioButtonId
            if (checked != View.NO_ID) {
                val tag = binding.radioGroup.findViewById<RadioButton>(checked).tag as Long
                viewModel.votar(tag,
                    onDuplicate = {
                        Snackbar.make(binding.root, R.string.already_voted, Snackbar.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        binding.layoutVote.visibility = View.GONE
                        binding.layoutConfirmation.visibility = View.VISIBLE
                        parentFragmentManager.setFragmentResult(RESULT_KEY, Bundle())
                    }
                )
            }
        }

        binding.buttonOk.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.dialog_width),
            resources.getDimensionPixelSize(R.dimen.dialog_height)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_VOTACION = "votacionId"
        const val RESULT_KEY = "vote_done"
        fun newInstance(votacionId: String) =
            VoteDialogFragment().apply { arguments = bundleOf(ARG_VOTACION to votacionId) }
    }
}

