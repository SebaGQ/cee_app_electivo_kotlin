package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentElectionsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ElectionsFragment : Fragment() {

    private var _binding: FragmentElectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ElectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElectionsBinding.inflate(inflater, container, false)
        val adapter = VotacionAdapter { votacion ->
            findNavController().navigate(
                R.id.action_elections_to_voteDetail,
                Bundle().apply { putString("votacionId", votacion.id) }
            )
        }
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.votaciones.collectLatest { list ->
                adapter.submit(list, viewModel.progress.value, viewModel.totalUsers.value)
            }
        }

        if (SessionManager.isAdmin()) {
            binding.toolbar.inflateMenu(R.menu.menu_elections)
            binding.toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_add_election) {
                    findNavController().navigate(R.id.action_elections_to_createElection)
                    true
                } else false
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_results) {
                Snackbar.make(binding.root, "Results sin implementar", Snackbar.LENGTH_SHORT).show()
                true
            } else true
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
