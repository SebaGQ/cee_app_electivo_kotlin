package com.kotlin.cee_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.data.UserRepository
import com.kotlin.cee_app.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepository.getInstance(this)

        binding.buttonLogin.setOnClickListener {
            val correo = binding.editCorreo.text.toString()
            val password = binding.editPassword.text.toString()
            lifecycleScope.launch {
                val usuario = repo.login(correo, password)
                if (usuario != null) {
                    SessionManager.currentUserId = usuario.id
                    SessionManager.currentUserRole = usuario.rol
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(binding.root, R.string.invalid_credentials, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
