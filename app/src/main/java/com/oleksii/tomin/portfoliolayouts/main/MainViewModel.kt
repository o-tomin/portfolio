package com.oleksii.tomin.portfoliolayouts.main

import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState

class MainViewModel : MviViewModel<MainState, MainEvents>(
    MainState(
        selectedBottomMenu = BottomMenuItem.PROFILE,
        isReady = false,
    )
) {
    init {
        updateState { copy(isReady = true) }
    }

    fun updateSelectedBottomMenu(bottomMenuItem: BottomMenuItem) = updateState {
        copy(
            selectedBottomMenu = bottomMenuItem,
        )
    }
}

data class MainState(
    val selectedBottomMenu: BottomMenuItem,
    val isReady: Boolean,
) : MviViewState

sealed class MainEvents {
    data class Error(val t: Throwable) : MainEvents()
}