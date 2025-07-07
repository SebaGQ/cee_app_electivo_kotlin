package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.kotlin.cee_app.ui.elections.viewmodel.VoteDetailViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.FragmentVoteDetailBinding

class VoteDetailFragment : DialogFragment() {

    private var _binding: FragmentVoteDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VoteDetailViewModel by viewModels()
    private val args: VoteDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoteDetailBinding.inflate(inflater, container, false)
        viewModel.cargar(args.votacionId)

        viewModel.votacion.asLiveData().observe(viewLifecycleOwner) { v ->
            binding.textPregunta.text = v?.titulo ?: ""
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
                    }
                )
            }
        }

        viewModel.opciones.asLiveData().observe(viewLifecycleOwner) { list ->
            binding.radioGroup.removeAllViews()
            list.forEach { opcion ->
                val view = inflater.inflate(R.layout.item_vote_option, binding.radioGroup, false)
                val rb = view.findViewById<RadioButton>(R.id.radioOption)
                rb.text = opcion.descripcion
                rb.tag = opcion.id
                binding.radioGroup.addView(rb)
            }
        }

        binding.buttonClose.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
