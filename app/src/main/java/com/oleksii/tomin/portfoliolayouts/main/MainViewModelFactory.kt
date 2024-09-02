package com.oleksii.tomin.portfoliolayouts.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory

object MainViewModelFactory : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}