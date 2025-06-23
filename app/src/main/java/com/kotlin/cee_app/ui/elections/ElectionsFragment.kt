package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kotlin.cee_app.ui.components.TopBar
import com.kotlin.cee_app.ui.elections.adapter.VotacionAdapter
import com.kotlin.cee_app.ui.elections.viewmodel.ElectionsViewModel
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
        TopBar.setup(binding.topBar, R.string.title_elections)

        val activeAdapter = VotacionAdapter { votacion ->
            findNavController().navigate(
                R.id.action_elections_to_voteDetail,
                Bundle().apply { putString("votacionId", votacion.id) }
            )
        }
        val pastAdapter = VotacionAdapter { }
        binding.recyclerActive.adapter = activeAdapter
        binding.recyclerPast.adapter = pastAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.active.collectLatest { list ->
                binding.textNoActive.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                activeAdapter.submit(
                    list,
                    viewModel.progress.value,
                    viewModel.totalUsers.value
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.past.collectLatest { list ->
                pastAdapter.submit(
                    list,
                    emptyMap(),
                    viewModel.totalUsers.value,
                    viewModel.winners.value
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.progress.collectLatest { map ->
                activeAdapter.submit(viewModel.active.value, map, viewModel.totalUsers.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.winners.collectLatest { map ->
                pastAdapter.submit(viewModel.past.value, emptyMap(), viewModel.totalUsers.value, map)
            }
        }

        if (SessionManager.isAdmin()) {
            TopBar.setup(
                binding.topBar,
                R.string.title_elections,
                R.menu.menu_elections
            ) {
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
