package com.oleksii.tomin.portfoliolayouts.main

import androidx.annotation.IdRes
import com.oleksii.tomin.portfoliolayouts.R

enum class BottomMenuItem(
    @IdRes val itemId: Int,
    val tag: String,
) {
    PROFILE(itemId = R.id.bottom_nav_profile, tag = "profile"),
    EXPERIENCE(itemId = R.id.bottom_nav_experience, tag = "experience"),
    EDUCATION(itemId = R.id.bottom_nav_education, tag = "education"),
}