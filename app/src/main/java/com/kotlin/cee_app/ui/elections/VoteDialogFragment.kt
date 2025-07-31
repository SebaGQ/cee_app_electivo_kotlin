package com.kotlin.cee_app.ui.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.entity.OpcionEntity
import com.kotlin.cee_app.databinding.DialogVoteBinding
import com.kotlin.cee_app.ui.elections.viewmodel.VoteDetailViewModel

class VoteDialogFragment : DialogFragment() {

    private var _binding: DialogVoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VoteDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogVoteBinding.inflate(inflater, container, false)
        val votacionId = requireArguments().getString(ARG_VOTACION) ?: ""
        viewModel.cargar(votacionId)

        setupObservers()
        setupClickListeners()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aplicar tema del diálogo para mejor apariencia
        setStyle(STYLE_NO_TITLE, R.style.Theme_CEEAPP_Dialog)
    }

    private fun setupObservers() {
        viewModel.yaVotoLiveData.observe(viewLifecycleOwner) { voted ->
            updateVoteButtonState(voted)
        }

        viewModel.votacionLiveData.observe(viewLifecycleOwner) { votacion ->
            binding.textPregunta.text = votacion?.titulo ?: ""
        }

        viewModel.opcionesLiveData.observe(viewLifecycleOwner) { opciones ->
            setupVoteOptions(opciones)
        }

        viewModel.opcionVotadaLiveData.observe(viewLifecycleOwner) { opcionId ->
            opcionId?.let { id ->
                val radio = binding.radioGroup.findViewWithTag<RadioButton>(id)
                radio?.isChecked = true
            }
        }

        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            updateLoadingState(isLoading)
        }

        viewModel.isVotingLiveData.observe(viewLifecycleOwner) { isVoting ->
            updateVotingState(isVoting)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            error?.let {
                showVoteError(it)
                viewModel.clearError()
            }
        }
    }

    private fun updateVoteButtonState(voted: Boolean) {
        val canVote = viewModel.canVote()

        binding.buttonVote.isEnabled = canVote && binding.radioGroup.checkedRadioButtonId != View.NO_ID

        when {
            voted -> {
                binding.buttonVote.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.disabled_gray)
                )
                binding.buttonVote.text = getString(R.string.already_voted)
            }
            !viewModel.isVotacionActiva() -> {
                binding.buttonVote.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.disabled_gray)
                )
                binding.buttonVote.text = "Votación cerrada"
                binding.buttonVote.isEnabled = false
            }
            else -> {
                binding.buttonVote.isEnabled = false // Se habilitará al seleccionar opción
                binding.buttonVote.text = getString(R.string.button_vote)
                binding.buttonVote.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.primary_dark)
                )
            }
        }
    }

    private fun setupVoteOptions(opciones: List<OpcionEntity>) {
        binding.radioGroup.removeAllViews()

        opciones.forEachIndexed { index, opcion ->
            val radioButton = layoutInflater.inflate(
                R.layout.item_vote_option,
                binding.radioGroup,
                false
            ) as RadioButton

            radioButton.text = opcion.descripcion
            radioButton.tag = opcion.id
            radioButton.id = View.generateViewId() // Generar ID único para cada RadioButton

            binding.radioGroup.addView(radioButton)
        }

        // Configurar listener para manejar cambios de selección
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Verificar que hay una opción seleccionada válida
            val hasValidSelection = checkedId != -1 && group.findViewById<RadioButton>(checkedId) != null

            // Actualizar estado del botón de votar
            binding.buttonVote.isEnabled = hasValidSelection && viewModel.canVote()

            // Debug: opcional, remover en producción
            if (hasValidSelection) {
                val selectedButton = group.findViewById<RadioButton>(checkedId)
                val selectedOpcionId = selectedButton.tag as Long
                android.util.Log.d("VoteDialog", "Opción seleccionada: ${selectedButton.text} (ID: $selectedOpcionId)")
            }
        }

        // Restaurar la opción previamente votada (si existe) o limpiar la selección
        viewModel.opcionVotada.value?.let { id ->
            binding.radioGroup.findViewWithTag<RadioButton>(id)?.isChecked = true
        } ?: binding.radioGroup.clearCheck()
    }

    private fun setupClickListeners() {
        binding.buttonCancel?.setOnClickListener {
            dismiss()
        }

        binding.buttonVote.setOnClickListener {
            handleVoteSubmission()
        }

        binding.buttonOk.setOnClickListener {
            dismiss()
        }
    }

    private fun handleVoteSubmission() {
        val checkedId = binding.radioGroup.checkedRadioButtonId

        if (checkedId != -1) {
            val selectedRadioButton = binding.radioGroup.findViewById<RadioButton>(checkedId)
            if (selectedRadioButton != null) {
                val opcionId = selectedRadioButton.tag as Long

                viewModel.votar(
                    opcionId,
                    onDuplicate = {
                        showVoteError(getString(R.string.already_voted))
                    },
                    onSuccess = {
                        showVoteSuccess()
                    },
                    onError = { errorMessage ->
                        showVoteError(errorMessage)
                    }
                )
            } else {
                showVoteError("Error: No se pudo identificar la opción seleccionada")
            }
        } else {
            showVoteError("Por favor seleccione una opción antes de votar")
        }
    }

    private fun showVoteSuccess() {
        binding.layoutVote.visibility = View.GONE
        binding.layoutConfirmation.visibility = View.VISIBLE

        // Notificar al fragment padre
        parentFragmentManager.setFragmentResult(RESULT_KEY, Bundle())
    }

    private fun updateLoadingState(isLoading: Boolean) {
        // Mostrar/ocultar indicador de carga si es necesario
        // Por ahora, deshabilitar interacciones durante la carga
        binding.radioGroup.isEnabled = !isLoading
        updateVoteButtonEnabledState()
    }

    private fun updateVotingState(isVoting: Boolean) {
        updateVoteButtonEnabledState()

        if (isVoting) {
            binding.buttonVote.text = getString(R.string.voting_in_progress)
        } else if (viewModel.yaVoto.value != true) {
            binding.buttonVote.text = getString(R.string.button_vote)
        }
    }

    private fun updateVoteButtonEnabledState() {
        val hasSelection = binding.radioGroup.checkedRadioButtonId != -1
        val canVote = viewModel.canVote()
        val isNotVoting = viewModel.isVoting.value != true
        val isNotLoading = viewModel.isLoading.value != true

        binding.buttonVote.isEnabled = hasSelection && canVote && isNotVoting && isNotLoading
    }

    private fun showVoteError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            // Configurar tamaño del diálogo
            setLayout(
                resources.getDimensionPixelSize(R.dimen.dialog_width),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Aplicar animaciones
            setWindowAnimations(R.style.DialogAnimation)

            // Hacer fondo transparente para mostrar las esquinas redondeadas
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_VOTACION = "votacionId"
        const val RESULT_KEY = "vote_done"

        fun newInstance(votacionId: String) = VoteDialogFragment().apply {
            arguments = bundleOf(ARG_VOTACION to votacionId)
        }
    }
}