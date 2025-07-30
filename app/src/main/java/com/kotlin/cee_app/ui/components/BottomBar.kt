package com.kotlin.cee_app.ui.components

import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kotlin.cee_app.R
import com.kotlin.cee_app.data.SessionManager

object BottomBar {
    fun setup(bottomNav: BottomNavigationView, navController: NavController) {
        bottomNav.apply {
            setupWithNavController(navController)
            menu.findItem(R.id.nav_users).isVisible = SessionManager.isAdmin()
        }
    }
}
