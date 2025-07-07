package com.kotlin.cee_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.UserRepository
import com.kotlin.cee_app.data.UsuarioEntity
import com.kotlin.cee_app.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Patterns

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepository.getInstance(this)

        binding.buttonCreate.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()

            val role = if (binding.switchAdmin.isChecked) "ADMIN" else "SIMPLE"

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                binding.editCorreo.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = UsuarioEntity(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    correo = correo,
                    password = password,
                    rol = role
                )
                repo.insertar(user)
                Snackbar.make(binding.root, R.string.account_created, Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
