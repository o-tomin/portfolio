package com.oleksii.tomin.portfoliolayouts.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.oleksii.tomin.portfoliolayouts.BaseActivity
import com.oleksii.tomin.portfoliolayouts.databinding.ActivityMainBinding
import com.oleksii.tomin.portfoliolayouts.education.EducationFragment
import com.oleksii.tomin.portfoliolayouts.experience.ExperienceFragment
import com.oleksii.tomin.portfoliolayouts.ext.scopedSelectAndDebounce
import com.oleksii.tomin.portfoliolayouts.ext.viewBinding
import com.oleksii.tomin.portfoliolayouts.profile.ProfileFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var profileFragment: Fragment
    private lateinit var experienceFragment: Fragment
    private lateinit var educationFragment: Fragment

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installAppSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider.create(
            owner = this,
            factory = MainViewModelFactory
        )[MainViewModel::class]
        profileFragment = ProfileFragment()
        experienceFragment = ExperienceFragment()
        educationFragment = EducationFragment()

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
            if (this@MainActivity::viewModel.isInitialized)
                !viewModel.currentState.isReady
            else false
        }
        setOnExitAnimationListener { screen ->
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
                    menuItem.mapToFragment(),
                    menuItem.tag
                )
                .addToBackStack(menuItem.tag)
                .commit()
        }
    }

    private fun BottomMenuItem.mapToFragment() = when (this) {
        BottomMenuItem.PROFILE -> profileFragment
        BottomMenuItem.EXPERIENCE -> experienceFragment
        BottomMenuItem.EDUCATION -> educationFragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}