package com.oleksii.tomin.portfoliolayouts.di

import com.oleksii.tomin.portfoliolayouts.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideMainViewModel() = MainViewModel()
}