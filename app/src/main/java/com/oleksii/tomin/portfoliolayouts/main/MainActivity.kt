package com.oleksii.tomin.portfoliolayouts.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.oleksii.tomin.portfoliolayouts.BaseActivity
import com.oleksii.tomin.portfoliolayouts.backpressedcallback.DoNothingOnBackPressedCallback
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
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var fragmentFactory: BottomMenuFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installAppSplashScreen(isKeepOnScreen = { false })
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(binding.root)

        with(binding) {
            with(viewModel) {

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
    }

    override fun onBackPressedCallback() = DoNothingOnBackPressedCallback
}