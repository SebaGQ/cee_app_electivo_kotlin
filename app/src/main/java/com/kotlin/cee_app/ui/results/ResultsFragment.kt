package com.kotlin.cee_app.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.kotlin.cee_app.ui.results.ResultsFragmentArgs
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kotlin.cee_app.databinding.FragmentResultsBinding
import com.kotlin.cee_app.ui.results.adapter.DashboardAdapter
import com.kotlin.cee_app.ui.results.adapter.OpcionResultAdapter
import com.kotlin.cee_app.ui.results.viewmodel.ResultsViewModel

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResultsViewModel by viewModels()
    private val args: ResultsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        val votacionId = args.votacionId
        if (votacionId.isNullOrEmpty()) {
            binding.recyclerDashboard.visibility = View.VISIBLE
            val adapter = DashboardAdapter()
            binding.recyclerDashboard.adapter = adapter
            viewModel.cargarDashboard()
            viewModel.dashboard.asLiveData().observe(viewLifecycleOwner) { list ->
                adapter.submit(list)
            }
        } else {
            binding.buttonBack.visibility = View.VISIBLE
            binding.buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }
            binding.chart.visibility = View.VISIBLE
            binding.recyclerOpciones.visibility = View.VISIBLE
            val adapter = OpcionResultAdapter()
            binding.recyclerOpciones.adapter = adapter
            viewModel.cargarResultados(votacionId)
            viewModel.datos.asLiveData().observe(viewLifecycleOwner) { list ->
                val entries = list.map { PieEntry(it.total.toFloat(), it.descripcion) }
                val dataSet = PieDataSet(entries, "")
                binding.chart.data = PieData(dataSet)
                binding.chart.invalidate()
                val total = list.sumOf { it.total }
                val percents = list.map {
                    val pct = if (total == 0) 0 else (it.total * 100 / total)
                    com.kotlin.cee_app.data.OpcionPercent(it.descripcion, pct)
                }
                adapter.submit(percents)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
