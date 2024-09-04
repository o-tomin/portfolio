package com.oleksii.tomin.portfoliolayouts.main

import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : MviViewModel<MainState, MainEvents>(
    MainState(
        selectedBottomMenu = BottomMenuItem.PROFILE,
        viewTransitioning = null,
    )
) {

    fun updateSelectedBottomMenu(bottomMenuItem: BottomMenuItem) = updateState {
        copy(
            viewTransitioning = ViewTransitioning.create(
                from = selectedBottomMenu,
                to = bottomMenuItem
            ),
            selectedBottomMenu = bottomMenuItem,
        )
    }
}

data class MainState(
    val selectedBottomMenu: BottomMenuItem,
    val viewTransitioning: ViewTransitioning?,
) : MviViewState

sealed class MainEvents {
    data class Error(val t: Throwable) : MainEvents()
}