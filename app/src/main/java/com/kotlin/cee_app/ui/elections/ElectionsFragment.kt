package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentElectionsBinding

/**
 * Fragmento principal que muestra las votaciones activas.
 */
class ElectionsFragment : Fragment() {

    private var _binding: FragmentElectionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ElectionsViewModel> { ElectionsViewModel.Factory(requireContext()) }
    private lateinit var adapter: ElectionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentElectionsBinding.inflate(inflater, container, false)
        requireActivity().title = "Ceecinf"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ElectionsAdapter { votacion ->
            val action = ElectionsFragmentDirections.actionElectionsToVoteDetail(votacion.id)
            findNavController().navigate(action)
        }
        binding.recyclerVotaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerVotaciones.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.votaciones.collect { list ->
                val progressMap = mutableMapOf<String, Int>()
                val total = viewModel.countUsuarios()
                list.forEach { v ->
                    val count = viewModel.countVotos(v.id)
                    val percent = if (total > 0) (count * 100) / total else 0
                    progressMap[v.id] = percent
                }
                adapter.submitList(list, progressMap)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (SessionManager.currentUser.rol == "ADMIN") {
            inflater.inflate(R.menu.menu_elections, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.action_elections_to_createElection)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
