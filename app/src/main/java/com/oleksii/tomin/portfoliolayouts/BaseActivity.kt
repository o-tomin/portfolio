package com.oleksii.tomin.portfoliolayouts

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.oleksii.tomin.portfoliolayouts.mvi.MviActivity

abstract class BaseActivity : MviActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback())
    }

    abstract fun onBackPressedCallback(): OnBackPressedCallback
}