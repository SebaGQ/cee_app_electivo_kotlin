package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
    ): View? {
        _binding = DialogVoteBinding.inflate(inflater, container, false)
        val id = requireArguments().getString(ARG_VOTACION_ID) ?: return binding.root
        viewModel.cargar(id)

        viewModel.votacion.asLiveData().observe(viewLifecycleOwner) { v ->
            binding.textPregunta.text = v?.titulo ?: ""
        }

        viewModel.opciones.asLiveData().observe(viewLifecycleOwner) { list ->
            binding.radioGroup.removeAllViews()
            list.forEach { opcion ->
                val item = inflater.inflate(R.layout.item_vote_option, binding.radioGroup, false)
                val rb = item.findViewById<RadioButton>(R.id.radioOption)
                rb.text = opcion.descripcion
                rb.tag = opcion.id
                binding.radioGroup.addView(item)
            }
        }

        binding.buttonVote.setOnClickListener {
            val checked = binding.radioGroup.checkedRadioButtonId
            if (checked != View.NO_ID) {
                val option = binding.radioGroup.findViewById<RadioButton>(checked).tag as Long
                viewModel.votar(option,
                    onDuplicate = {
                        Snackbar.make(binding.root, R.string.already_voted, Snackbar.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        binding.layoutVote.visibility = View.GONE
                        binding.layoutConfirmation.visibility = View.VISIBLE
                    }
                )
            }
        }

        binding.buttonClose.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_VOTACION_ID = "votacionId"
        fun newInstance(id: String): VoteDialogFragment = VoteDialogFragment().apply {
            arguments = Bundle().apply { putString(ARG_VOTACION_ID, id) }
        }
    }
}
