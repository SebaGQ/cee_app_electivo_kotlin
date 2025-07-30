package com.kotlin.cee_app.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.FragmentUsersBinding
import com.kotlin.cee_app.ui.users.adapter.UserAdapter
import com.kotlin.cee_app.ui.users.viewmodel.UsersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)

        val adapter = UserAdapter(
            onEdit = { user ->
                findNavController().navigate(
                    R.id.action_users_to_createUser,
                    Bundle().apply { putString("userId", user.id) }
                )
            },
            onDelete = { user ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.confirm_delete)
                    .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.eliminar(user.id) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
        )
        binding.recyclerUsers.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usuarios.collectLatest { list ->
                adapter.submit(list)
            }
        }

        if (SessionManager.isAdmin()) {
            binding.fabAddContainer.visibility = View.VISIBLE
            binding.fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_users_to_createUser)
            }
        } else {
            binding.fabAddContainer.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
