package com.jgcoding.kotlin.commercelistapp.core

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat

object Extensions {

    fun Activity.setStatusBar() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.WHITE
        WindowInsetsControllerCompat(
            getWindow(),
            getWindow().decorView
        ).isAppearanceLightStatusBars = true
    }
}