package com.oleksii.tomin.portfoliolayouts.main

import com.oleksii.tomin.portfoliolayouts.mvi.MviViewModel
import com.oleksii.tomin.portfoliolayouts.mvi.MviViewState

class MainViewModel : MviViewModel<MainState, MainEvents>(
    MainState(
        selectedBottomMenu = BottomMenuItem.PROFILE,
        viewTransitioning = null,
        isReady = false,
    )
) {
    init {
        updateState { copy(isReady = true) }
    }

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
    val isReady: Boolean,
) : MviViewState

data class ViewTransitioning(
    val enter: ViewTransitioningAnimation,
    val exit: ViewTransitioningAnimation,
    val popEnter: ViewTransitioningAnimation,
    val popExit: ViewTransitioningAnimation,
) {
    companion object {
        fun create(
            from: BottomMenuItem,
            to: BottomMenuItem,
        ) =
            if (from < to) {
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_RIGHT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_LEFT,
                    popEnter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    popExit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                )
            } else {
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popEnter = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popExit = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                )
            }
    }
}

sealed class MainEvents {
    data class Error(val t: Throwable) : MainEvents()
}