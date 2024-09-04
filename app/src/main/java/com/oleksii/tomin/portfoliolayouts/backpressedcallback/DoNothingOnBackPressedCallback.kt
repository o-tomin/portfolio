package com.oleksii.tomin.portfoliolayouts.backpressedcallback

import androidx.activity.OnBackPressedCallback

object DoNothingOnBackPressedCallback : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {}
}