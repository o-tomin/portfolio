package com.oleksii.tomin.portfoliolayouts

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.oleksii.tomin.portfoliolayouts.mvi.MviActivity

abstract class BaseActivity : MviActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback())
    }

    abstract fun onBackPressedCallback(): OnBackPressedCallback
}