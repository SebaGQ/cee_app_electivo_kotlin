package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlin.cee_app.databinding.FragmentVoteConfirmationBinding

class VoteConfirmationFragment : Fragment() {

    private var _binding: FragmentVoteConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoteConfirmationBinding.inflate(inflater, container, false)
        binding.buttonResults.setOnClickListener {
            // Navegar a resultados (no implementado)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
