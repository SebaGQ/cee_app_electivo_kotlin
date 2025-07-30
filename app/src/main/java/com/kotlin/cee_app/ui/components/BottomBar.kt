package com.kotlin.cee_app.ui.components

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.kotlin.cee_app.R

object BottomBar {
    fun setup(bottomBar: BottomNavigationView, navController: NavController) {
        val context = bottomBar.context
        bottomBar.setBackgroundColor(
            ContextCompat.getColor(context, R.color.primary_blue)
        )
        val white = ContextCompat.getColor(context, R.color.white)
        val whiteState = ColorStateList.valueOf(white)
        bottomBar.itemIconTintList = whiteState
        bottomBar.itemTextColor = whiteState
        bottomBar.setupWithNavController(navController)
    }
}
