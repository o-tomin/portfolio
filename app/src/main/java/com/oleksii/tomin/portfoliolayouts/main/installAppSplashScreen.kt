package com.oleksii.tomin.portfoliolayouts.main

import android.animation.ObjectAnimator
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

fun Activity.installAppSplashScreen(
    isKeepOnScreen: () -> Boolean
) =
    installSplashScreen().apply {
        setKeepOnScreenCondition {
            isKeepOnScreen()
        }
        setOnExitAnimationListener { screen ->
            try {
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            } catch (t: Throwable) {
                Log.e("Alex", t.message, t)
            }
        }
    }