package com.oleksii.tomin.portfoliolayouts.main

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.oleksii.tomin.portfoliolayouts.R
import com.oleksii.tomin.portfoliolayouts.education.EducationFragment
import com.oleksii.tomin.portfoliolayouts.experience.ExperienceFragment
import com.oleksii.tomin.portfoliolayouts.profile.ProfileFragment

enum class BottomMenuItem(
    @IdRes val itemId: Int,
    val tag: String,
    val associatedFragment: Class<out Fragment>
) {

    PROFILE(
        itemId = R.id.bottom_nav_profile,
        tag = "profile",
        associatedFragment = ProfileFragment::class.java,
    ),
    EXPERIENCE(
        itemId = R.id.bottom_nav_experience,
        tag = "experience",
        associatedFragment = ExperienceFragment::class.java,
    ),
    EDUCATION(
        itemId = R.id.bottom_nav_education,
        tag = "education",
        associatedFragment = EducationFragment::class.java,
    );

    fun associatedFragmentClassName(): String =
        associatedFragment.name
}