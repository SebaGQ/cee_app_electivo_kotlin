package com.kotlin.cee_app.ui.components

import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kotlin.cee_app.R

object TopBar {
    fun setup(
        toolbar: Toolbar,
        @StringRes titleRes: Int,
        @MenuRes menuRes: Int? = null,
        onMenuItemClick: ((MenuItem) -> Boolean)? = null
    ) {
        toolbar.setTitle(titleRes)
        toolbar.setTitleTextColor(
            ContextCompat.getColor(toolbar.context, R.color.white)
        )
        menuRes?.let {
            toolbar.menu.clear()
            toolbar.inflateMenu(it)
            onMenuItemClick?.let { listener ->
                toolbar.setOnMenuItemClickListener(listener)
            }
        }
    }
}
