package com.globant.ticketmaster.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ViewEffect

interface ViewEvent

abstract class BaseViewModel<Event : ViewEvent, Effect : ViewEffect> : ViewModel() {
    private val _effect = Channel<Effect>()
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    private val event: MutableSharedFlow<Event> = MutableSharedFlow()

    init {
        subscribeToEvents()
    }

    abstract fun onTriggerEvent(event: Event)

    fun setEvent(newEvent: Event) {
        viewModelScope.launch {
            event.emit(newEvent)
        }
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            event.collect {
                onTriggerEvent(it)
            }
        }
    }

    protected fun setEffect(newEffect: () -> Effect) {
        viewModelScope.launch { _effect.trySend(newEffect()) }
    }
}

@NonRestartableComposable
@Composable
fun <Event : ViewEvent, Effect : ViewEffect> LaunchViewEffect(
    viewModel: BaseViewModel<Event, Effect>,
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    onEffect: suspend (viewEffect: Effect) -> Unit,
) {
    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(lifecycleState) {
            withContext(Dispatchers.Main.immediate) {
                viewModel.effect
                    .onEach { viewEffect ->
                        onEffect(viewEffect)
                    }.collect()
            }
        }
    }
}
