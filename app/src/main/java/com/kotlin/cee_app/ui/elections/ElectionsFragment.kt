package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kotlin.cee_app.ui.elections.adapter.VotacionAdapter
import com.kotlin.cee_app.ui.elections.viewmodel.ElectionsViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentElectionsBinding
import com.kotlin.cee_app.ui.elections.VoteDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        childFragmentManager.setFragmentResultListener(
            VoteDialogFragment.RESULT_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.refresh()
            }
        }

        val activeAdapter = VotacionAdapter(
            onClick = { votacion ->
                VoteDialogFragment.newInstance(votacion.id)
                    .show(childFragmentManager, "vote")
            },
            onEdit = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_createElection,
                    Bundle().apply { putString("votacionId", votacion.id) }
                )
            },
            onDelete = { votacion ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.confirm_delete)
                    .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.eliminar(votacion) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            },
            onFinalize = { votacion -> viewModel.finalizar(votacion) }
        )

        val upcomingAdapter = VotacionAdapter(
            onClick = { votacion ->
                VoteDialogFragment.newInstance(votacion.id)
                    .show(childFragmentManager, "vote")
            },
            onEdit = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_createElection,
                    Bundle().apply { putString("votacionId", votacion.id) }
                )
            },
            onDelete = { votacion ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.confirm_delete)
                    .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.eliminar(votacion) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            },
            onFinalize = { votacion -> viewModel.finalizar(votacion) }
        )

        val pastAdapter = VotacionAdapter(
            onClick = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_results,
                    Bundle().apply { putString("votacionId", votacion.id) }
                )
            },
            onEdit = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_createElection,
                    Bundle().apply { putString("votacionId", votacion.id) }
                )
            },
            onDelete = { votacion ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.confirm_delete)
                    .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.eliminar(votacion) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            },
            onFinalize = { votacion -> viewModel.finalizar(votacion) }
        )

        binding.recyclerActive.adapter = activeAdapter
        binding.recyclerUpcoming.adapter = upcomingAdapter
        binding.recyclerPast.adapter = pastAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.active.collectLatest { list ->
                binding.textActiveHeader.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
                binding.textNoActive.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                activeAdapter.submit(
                    list,
                    viewModel.progress.value,
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = viewModel.voted.value,
                    votedOptionMap = viewModel.votedOption.value
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcoming.collectLatest { list ->
                binding.textUpcomingHeader.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
                upcomingAdapter.submit(
                    list,
                    emptyMap(),
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = emptyMap(),
                    votedOptionMap = emptyMap()
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.past.collectLatest { list ->
                binding.textPastHeader.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
                pastAdapter.submit(
                    list,
                    emptyMap(),
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = emptyMap(),
                    votedOptionMap = emptyMap()
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.progress.collectLatest { map ->
                activeAdapter.submit(
                    viewModel.active.value,
                    map,
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = viewModel.voted.value,
                    votedOptionMap = viewModel.votedOption.value
                )
                pastAdapter.submit(
                    viewModel.past.value,
                    map,
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = viewModel.voted.value,
                    votedOptionMap = viewModel.votedOption.value
                )
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.optionsPercent.collectLatest { opts ->
                activeAdapter.submit(
                    viewModel.active.value,
                    viewModel.progress.value,
                    opts,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = viewModel.voted.value,
                    votedOptionMap = viewModel.votedOption.value
                )
                upcomingAdapter.submit(
                    viewModel.upcoming.value,
                    emptyMap(),
                    opts,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = emptyMap(),
                    votedOptionMap = emptyMap()
                )
                pastAdapter.submit(
                    viewModel.past.value,
                    viewModel.progress.value,
                    opts,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = viewModel.voted.value,
                    votedOptionMap = viewModel.votedOption.value
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.voted.collectLatest { votedMap ->
                activeAdapter.submit(
                    viewModel.active.value,
                    viewModel.progress.value,
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = votedMap,
                    votedOptionMap = viewModel.votedOption.value
                )
                pastAdapter.submit(
                    viewModel.past.value,
                    viewModel.progress.value,
                    viewModel.optionsPercent.value,
                    viewModel.optionsCount.value,
                    viewModel.totalUsers.value,
                    votedMap = votedMap,
                    votedOptionMap = viewModel.votedOption.value
                )
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.refresh()
                binding.swipeRefresh.isRefreshing = false
            }
        }

        if (SessionManager.isAdmin()) {
            binding.fabCreateElection.visibility = View.VISIBLE
            binding.fabCreateElection.setOnClickListener {
                findNavController().navigate(R.id.action_elections_to_createElection)
            }
        } else {
            binding.fabCreateElection.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
