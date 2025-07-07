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

        val activeAdapter = VotacionAdapter(
            onClick = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_voteDetail,
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
                viewModel.eliminar(votacion)
            }
        )

        val upcomingAdapter = VotacionAdapter(
            onClick = { votacion ->
                findNavController().navigate(
                    R.id.action_elections_to_voteDetail,
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
                viewModel.eliminar(votacion)
            }
        )

        binding.recyclerActive.adapter = activeAdapter
        binding.recyclerUpcoming.adapter = upcomingAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.active.collectLatest { list ->
                binding.textNoActive.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                activeAdapter.submit(
                    list,
                    viewModel.progress.value,
                    viewModel.optionsPercent.value,
                    viewModel.totalUsers.value
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
                    viewModel.totalUsers.value
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.progress.collectLatest { map ->
                activeAdapter.submit(
                    viewModel.active.value,
                    map,
                    viewModel.optionsPercent.value,
                    viewModel.totalUsers.value
                )
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.optionsPercent.collectLatest { opts ->
                activeAdapter.submit(
                    viewModel.active.value,
                    viewModel.progress.value,
                    opts,
                    viewModel.totalUsers.value
                )
                upcomingAdapter.submit(
                    viewModel.upcoming.value,
                    emptyMap(),
                    opts,
                    viewModel.totalUsers.value
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
            binding.fabMain.visibility = View.VISIBLE
            binding.fabMain.setOnClickListener {
                binding.fabCreate.visibility =
                    if (binding.fabCreate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            binding.fabCreate.setOnClickListener {
                binding.fabCreate.visibility = View.GONE
                findNavController().navigate(R.id.action_elections_to_createElection)
            }
        } else {
            binding.fabMain.visibility = View.GONE
            binding.fabCreate.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
