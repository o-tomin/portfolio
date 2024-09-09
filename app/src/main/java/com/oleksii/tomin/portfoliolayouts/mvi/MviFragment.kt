package com.oleksii.tomin.portfoliolayouts.mvi

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.oleksii.tomin.portfoliolayouts.ext.collectWithLifecycle
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

open class MviFragment : Fragment() {
    val <S : MviViewState> MviViewModel<S, *>.currentState
        get() = state.value

    fun <S : MviViewState, P> MviViewModel<S, *>.collectStateProperty(
        prop: (S) -> P,
        owner: LifecycleOwner = this@MviFragment,
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        collector: FlowCollector<P>
    ) =
        state
            .map { prop(it) }
            .distinctUntilChanged()
            .collectWithLifecycle(owner, minState, collector)


    fun <E : Any> MviViewModel<*, E>.collectEvents(
        owner: LifecycleOwner = this@MviFragment,
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        collector: FlowCollector<E>
    ) =
        events
            .collectWithLifecycle(owner, minState, collector)
}