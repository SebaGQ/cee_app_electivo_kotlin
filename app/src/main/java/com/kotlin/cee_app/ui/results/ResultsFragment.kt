package com.kotlin.cee_app.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.kotlin.cee_app.databinding.FragmentResultsEnhancedBinding
import com.kotlin.cee_app.ui.results.adapter.ExtendedDashboardAdapter
import com.kotlin.cee_app.ui.results.adapter.OpcionResultAdapter
import com.kotlin.cee_app.ui.results.adapter.VotacionParticipationAdapter
import com.kotlin.cee_app.ui.results.viewmodel.ResultsViewModel

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsEnhancedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResultsViewModel by viewModels()
    private val args: ResultsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsEnhancedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val votacionId = args.votacionId

        if (votacionId.isNullOrEmpty()) {
            setupDashboardView()
        } else {
            setupVotacionResultsView(votacionId)
        }

        observeViewModel()
    }

    private fun setupDashboardView() {
        // Ocultar vista de resultados específicos
        binding.layoutVotacionResults.isVisible = false
        binding.layoutDashboard.isVisible = true

        // Configurar adaptadores
        val dashboardAdapter = ExtendedDashboardAdapter()
        binding.recyclerDashboardMetrics.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = dashboardAdapter
        }

        val participationAdapter = VotacionParticipationAdapter()
        binding.recyclerVotacionesParticipation.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = participationAdapter
        }

        // Cargar datos del dashboard
        viewModel.cargarDashboardCompleto()
    }

    private fun setupVotacionResultsView(votacionId: String) {
        // Ocultar vista de dashboard
        binding.layoutDashboard.isVisible = false
        binding.layoutVotacionResults.isVisible = true

        // Configurar adaptador de opciones
        val adapter = OpcionResultAdapter()
        binding.recyclerOpciones.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        // Configurar el gráfico
        setupPieChart()

        // Cargar resultados
        viewModel.cargarResultados(votacionId)
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)

            dragDecelerationFrictionCoef = 0.95f

            isDrawHoleEnabled = true
            setHoleColor(android.graphics.Color.WHITE)
            setTransparentCircleColor(android.graphics.Color.WHITE)
            setTransparentCircleAlpha(110)

            holeRadius = 58f
            transparentCircleRadius = 61f

            setDrawCenterText(true)
            centerText = "Resultados"

            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            animateY(1400, Easing.EaseInOutQuad)

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 0f
            }

            setEntryLabelColor(android.graphics.Color.BLACK)
            setEntryLabelTextSize(12f)
        }
    }

    private fun observeViewModel() {
        // Observar estado de carga
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        // Observar datos del dashboard
        viewModel.dashboardItemsLiveData.observe(viewLifecycleOwner) { items ->
            (binding.recyclerDashboardMetrics.adapter as? ExtendedDashboardAdapter)?.submitList(items)
        }

        // Observar votaciones con participación
        viewModel.votacionesConParticipacionLiveData.observe(viewLifecycleOwner) { votaciones ->
            (binding.recyclerVotacionesParticipation.adapter as? VotacionParticipationAdapter)?.submitList(votaciones)
        }

        // Observar datos de votación específica
        viewModel.datosLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                updatePieChart(list)
                updateOptionsList(list)
            }
        }

        // Observar dashboard data para actualizar título
        viewModel.dashboardDataLiveData.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.textDashboardSummary.text = buildString {
                    append("Sistema de votaciones con ${it.totalUsuarios} usuarios registrados. ")
                    append("Participación promedio: %.1f%%".format(it.porcentajeParticipacion))
                }
            }
        }
    }

    private fun updatePieChart(data: List<com.kotlin.cee_app.data.ConteoOpcion>) {
        val entries = data.map { PieEntry(it.total.toFloat(), it.descripcion) }

        val dataSet = PieDataSet(entries, "").apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = com.github.mikephil.charting.utils.MPPointF(0f, 40f)
            selectionShift = 5f

            // Agregar colores
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        val pieData = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(binding.pieChart))
            setValueTextSize(11f)
            setValueTextColor(android.graphics.Color.BLACK)
        }

        binding.pieChart.data = pieData
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }

    private fun updateOptionsList(data: List<com.kotlin.cee_app.data.ConteoOpcion>) {
        val total = data.sumOf { it.total }
        val percents = data.map {
            val pct = if (total == 0) 0 else (it.total * 100 / total)
            com.kotlin.cee_app.data.OpcionPercent(it.descripcion, pct)
        }
        (binding.recyclerOpciones.adapter as? OpcionResultAdapter)?.submit(percents)

        binding.textTotalVotes.text = "Total de votos: $total"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}