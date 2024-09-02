package com.oleksii.tomin.portfoliolayouts.main

import androidx.annotation.AnimRes
import com.oleksii.tomin.portfoliolayouts.R

enum class ViewTransitioningAnimation(
    @AnimRes val itemId: Int,
) {
    SLIDE_IN_LEFT(R.anim.slide_in_left),
    SLIDE_IN_RIGHT(R.anim.slide_in_right),
    SLIDE_OUT_LEFT(R.anim.slide_out_left),
    SLIDE_OUT_RIGHT(R.anim.slide_out_right),

}