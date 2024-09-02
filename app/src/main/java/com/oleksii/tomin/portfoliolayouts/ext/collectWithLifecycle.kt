package com.oleksii.tomin.portfoliolayouts.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectWithLifecycle(
    owner: LifecycleOwner,
    minState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: FlowCollector<T>
) = owner.lifecycleScope.launch {
    owner.lifecycle.whenStateAtLeast(minState) {
        collect(collector)
    }
}
