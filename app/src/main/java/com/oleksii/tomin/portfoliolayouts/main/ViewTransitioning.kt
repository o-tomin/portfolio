package com.oleksii.tomin.portfoliolayouts.main

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