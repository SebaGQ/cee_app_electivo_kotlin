package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.asLiveData
import com.kotlin.cee_app.R
import com.kotlin.cee_app.databinding.FragmentVoteDetailBinding

class VoteDetailFragment : Fragment() {

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
                viewModel.votar(tag)
                findNavController().navigate(R.id.action_vote_to_confirmation)
            }
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
