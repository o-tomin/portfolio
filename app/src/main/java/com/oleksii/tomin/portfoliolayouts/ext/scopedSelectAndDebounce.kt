package com.oleksii.tomin.portfoliolayouts.ext

import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
fun NavigationBarView.scopedSelectAndDebounce(debounceTime: Long = 300L) =
    selects().debounce(debounceTime)