package com.oleksii.tomin.portfoliolayouts.main

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class ViewTransitioningTest {

    @ParameterizedTest
    @MethodSource("transitioningAnimationDirectionTestData")
    fun testTransitioningAnimationDirections(
        from: BottomMenuItem,
        to: BottomMenuItem,
        expected: ViewTransitioning
    ) {
        Assertions.assertEquals(
            expected,
            ViewTransitioning.create(from, to),
        )
    }

    companion object {

        @JvmStatic
        fun transitioningAnimationDirectionTestData() = listOf(
            Arguments.of(
                BottomMenuItem.PROFILE,
                BottomMenuItem.EXPERIENCE,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_RIGHT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_LEFT,
                    popEnter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    popExit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                )
            ),
            Arguments.of(
                BottomMenuItem.PROFILE,
                BottomMenuItem.EDUCATION,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_RIGHT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_LEFT,
                    popEnter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    popExit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                )
            ),
            Arguments.of(
                BottomMenuItem.EXPERIENCE,
                BottomMenuItem.PROFILE,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popEnter = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popExit = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                )
            ),
            Arguments.of(
                BottomMenuItem.EXPERIENCE,
                BottomMenuItem.EDUCATION,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_RIGHT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_LEFT,
                    popEnter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    popExit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                )
            ),
            Arguments.of(
                BottomMenuItem.EDUCATION,
                BottomMenuItem.PROFILE,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popEnter = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popExit = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                )
            ),
            Arguments.of(
                BottomMenuItem.EDUCATION,
                BottomMenuItem.EXPERIENCE,
                ViewTransitioning(
                    enter = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                    exit = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popEnter = ViewTransitioningAnimation.SLIDE_OUT_RIGHT,
                    popExit = ViewTransitioningAnimation.SLIDE_IN_LEFT,
                )
            ),
        )
    }
}