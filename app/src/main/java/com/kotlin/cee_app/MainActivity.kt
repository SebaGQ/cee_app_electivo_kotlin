package com.kotlin.cee_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.kotlin.cee_app.data.SessionManager
import com.kotlin.cee_app.databinding.ActivityMainBinding
import com.kotlin.cee_app.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        if (SessionManager.currentUserId.isBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.includeTopBar.topBar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val topLevel = if (SessionManager.isAdmin()) {
            setOf(R.id.nav_elections, R.id.nav_results, R.id.nav_users)
        } else {
            setOf(R.id.nav_elections, R.id.nav_results)
        }
        appBarConfiguration = AppBarConfiguration(topLevel)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.appBarMain.bottomNav.setupWithNavController(navController)
        binding.appBarMain.bottomNav.menu.findItem(R.id.nav_users).isVisible = SessionManager.isAdmin()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_topbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                SessionManager.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}