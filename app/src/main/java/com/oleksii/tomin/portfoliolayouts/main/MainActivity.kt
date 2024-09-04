package com.oleksii.tomin.portfoliolayouts.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.oleksii.tomin.portfoliolayouts.BaseActivity
import com.oleksii.tomin.portfoliolayouts.databinding.ActivityMainBinding
import com.oleksii.tomin.portfoliolayouts.di.BottomMenuFragmentFactory
import com.oleksii.tomin.portfoliolayouts.ext.scopedSelectAndDebounce
import com.oleksii.tomin.portfoliolayouts.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)

    @Inject
    lateinit var fragmentFactory: BottomMenuFragmentFactory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installAppSplashScreen()
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(binding.root)

        viewModel = ViewModelProvider.create(
            owner = this,
            factory = MainViewModelFactory
        )[MainViewModel::class]

        binding.setUpBindings()
        viewModel.setUpCollectors()
    }

    override fun onBackPressedCallback() = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Do nothing on back pressed
        }
    }

    private fun installAppSplashScreen() = installSplashScreen().apply {
        setKeepOnScreenCondition {
            !this@MainActivity::viewModel.isInitialized
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

    private fun ActivityMainBinding.setUpBindings() {
        bottomNavigation.scopedSelectAndDebounce()
            .mapNotNull { menuItem ->
                enumValues<BottomMenuItem>().firstOrNull {
                    it.itemId == menuItem.itemId
                }
            }
            .onEach { menuItem ->
                viewModel.updateSelectedBottomMenu(menuItem)
            }
            .launchIn(lifecycleScope)
    }

    private fun MainViewModel.setUpCollectors() = with(binding) {
        collectStateProperty(MainState::selectedBottomMenu) { menuItem ->
            supportFragmentManager.beginTransaction().apply transaction@{
                currentState.viewTransitioning?.let { transitioning ->
                    this@transaction.setCustomAnimations(
                        transitioning.enter.itemId,
                        transitioning.exit.itemId,
                        transitioning.popEnter.itemId,
                        transitioning.popExit.itemId,
                    )
                }
            }
                .replace(
                    contentMenuContainer.id,
                    supportFragmentManager.fragmentFactory.instantiate(
                        classLoader,
                        menuItem.associatedFragmentClassName()
                    ),
                    menuItem.tag
                )
                .addToBackStack(menuItem.tag)
                .commit()
        }
    }
}