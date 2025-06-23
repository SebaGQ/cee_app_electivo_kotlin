package com.kotlin.cee_app.ui.components

import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar

object TopBar {
    fun setup(
        toolbar: Toolbar,
        @StringRes titleRes: Int,
        @MenuRes menuRes: Int? = null,
        onMenuItemClick: ((MenuItem) -> Boolean)? = null
    ) {
        toolbar.setTitle(titleRes)
        menuRes?.let {
            toolbar.menu.clear()
            toolbar.inflateMenu(it)
            onMenuItemClick?.let { listener ->
                toolbar.setOnMenuItemClickListener(listener)
            }
        }
    }
}
