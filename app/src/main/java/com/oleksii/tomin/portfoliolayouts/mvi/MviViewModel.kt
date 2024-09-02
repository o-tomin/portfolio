package com.oleksii.tomin.portfoliolayouts.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The Base ViewModel class your ViewModel should inherit from
 *
 */
abstract class MviViewModel<S : MviViewState, E : Any>(
    initialState: S
) : ViewModel() {

    val state: StateFlow<S>
        get() = _state

    val events: Flow<E>
        get() = _events

    private val _state = MutableStateFlow(initialState)
    private val _events = MutableSharedFlow<E>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    private val stateMutex = Mutex()

    protected suspend fun setState(reducer: S.() -> S) {
        _state.value = stateMutex.withLock {
            _state.value.reducer()
        }
    }

    protected fun updateState(reducer: S.() -> S) {
        viewModelScope.launch {
            setState(reducer)
        }
    }

    protected fun sendEvent(event: E) {
        _events.tryEmit(event)
    }

    /**
     * Binds provided Flow to update ViewModel state
     */
    protected fun <T> Flow<T>.bind(reducer: S.(value: T) -> S) {
        onEach { value ->
            setState {
                reducer(value)
            }
        }.launchIn(viewModelScope)
    }
}

interface MviViewState
