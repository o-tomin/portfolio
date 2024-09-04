package com.oleksii.tomin.portfoliolayouts.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.oleksii.tomin.portfoliolayouts.education.EducationFragment
import com.oleksii.tomin.portfoliolayouts.experience.ExperienceFragment
import com.oleksii.tomin.portfoliolayouts.main.BottomMenuItem
import com.oleksii.tomin.portfoliolayouts.profile.ProfileFragment
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BottomMenuFragmentFactory @Inject constructor(
    private val profileFragment: ProfileFragment,
    private val experienceFragment: ExperienceFragment,
    private val educationFragment: EducationFragment
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val menuItem = enumValues<BottomMenuItem>()
            .firstOrNull { it.associatedFragmentClassName() == className }

        if (menuItem == null)
            throw IllegalArgumentException("No bottom menu associated with $className")

        return when (menuItem) {
            BottomMenuItem.PROFILE -> profileFragment
            BottomMenuItem.EXPERIENCE -> experienceFragment
            BottomMenuItem.EDUCATION -> educationFragment
        }
    }
}