package com.oleksii.tomin.portfoliolayouts.di

import android.content.Context
import com.oleksii.tomin.portfoliolayouts.main.MainViewModel
import com.oleksii.tomin.portfoliolayouts.profile.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideMainViewModel() = MainViewModel()

    @Provides
    fun provideProfileViewModel(
        context: Context,
    ) =
        ProfileViewModel(
            context = context,
        )
}