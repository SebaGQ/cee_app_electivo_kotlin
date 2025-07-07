package com.kotlin.cee_app

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.UserRepository
import com.kotlin.cee_app.data.UsuarioEntity
import com.kotlin.cee_app.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepository.getInstance(this)

        binding.buttonCreate.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Snackbar.make(binding.root, R.string.invalid_email, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                repo.insertar(
                    UsuarioEntity(
                        id = UUID.randomUUID().toString(),
                        nombre = nombre,
                        correo = correo,
                        password = password,
                        rol = "SIMPLE"
                    )
                )
                Snackbar.make(binding.root, R.string.account_created, Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
