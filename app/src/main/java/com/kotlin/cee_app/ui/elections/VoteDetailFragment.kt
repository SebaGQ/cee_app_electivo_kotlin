package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kotlin.cee_app.ui.elections.viewmodel.VoteDetailViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.asLiveData
import android.content.res.ColorStateList
import com.kotlin.cee_app.ui.elections.VoteDetailFragmentDirections
import com.google.android.material.snackbar.Snackbar
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

        viewModel.yaVoto.asLiveData().observe(viewLifecycleOwner) { voted ->
            binding.buttonVote.isEnabled = !voted
            if (voted) {
                binding.buttonVote.backgroundTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.disabled_gray))
            }
        }

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
                        val action = VoteDetailFragmentDirections.actionVoteToConfirmation(args.votacionId)
                        findNavController().navigate(action)
                    }
                )
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
            // Restaurar selección previa si existe
            viewModel.opcionVotada.value?.let { id ->
                binding.radioGroup.findViewWithTag<RadioButton>(id)?.isChecked = true
            }
        }

        // Observar cambios en la opción votada para mantener el check al
        // recrear la vista (p. ej. al rotar la pantalla)
        viewModel.opcionVotada.asLiveData().observe(viewLifecycleOwner) { id ->
            id?.let {
                binding.radioGroup.findViewWithTag<RadioButton>(it)?.isChecked = true
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
