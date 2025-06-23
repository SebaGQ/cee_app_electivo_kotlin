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
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentVoteDetailBinding

/**
 * Permite votar en una votación.
 */
class VoteDetailFragment : Fragment() {

    private var _binding: FragmentVoteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: VoteDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<VoteDetailViewModel> {
        VoteDetailViewModel.Factory(requireContext(), args.votacionId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonVote.setOnClickListener {
            val checkedId = binding.radioGroup.checkedRadioButtonId
            if (checkedId == View.NO_ID) {
                Snackbar.make(it, "Seleccione una opción", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rb = view.findViewById<RadioButton>(checkedId)
            val opcionId = rb.tag as Long
            viewModel.votar(opcionId, SessionManager.currentUser.id)
            findNavController().navigate(R.id.action_voteDetail_to_voteConfirmation)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.opciones.collect { opciones ->
                binding.radioGroup.removeAllViews()
                opciones.forEach { opcion ->
                    val rb = RadioButton(requireContext()).apply {
                        text = opcion.descripcion
                        tag = opcion.id
                    }
                    binding.radioGroup.addView(rb)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
