package com.oleksii.tomin.portfoliolayouts.di

import androidx.fragment.app.FragmentFactory
import com.oleksii.tomin.portfoliolayouts.education.EducationFragment
import com.oleksii.tomin.portfoliolayouts.experience.ExperienceFragment
import com.oleksii.tomin.portfoliolayouts.profile.ProfileFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object FragmentModule {

    @Provides
    fun provideBottomMenuFragmentFactory(
        profileFragment: ProfileFragment,
        experienceFragment: ExperienceFragment,
        educationFragment: EducationFragment,
    ): FragmentFactory {
        return BottomMenuFragmentFactory(
            profileFragment,
            experienceFragment,
            educationFragment,
        )
    }

    @Provides
    fun provideProfileFragment() = ProfileFragment()

    @Provides
    fun provideExperienceFragment() = ExperienceFragment()

    @Provides
    fun provideEducationFragment() = EducationFragment()
}