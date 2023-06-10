package io.chaldeaprjkt.yumetsuki.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.chaldeaprjkt.yumetsuki.ui.events.Event
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEvent
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val localEventContainer: LocalEventContainer,
) : ViewModel() {
    private var _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    val localEvent
        get() = localEventContainer.event

    fun Event.emit() =
        viewModelScope.launch {
            if (this is LocalEvent) {
                localEventContainer.emit(this@emit as LocalEvent)
            } else {
                _event.emit(this@emit)
            }
        }

    fun LocalEvent.emit() = viewModelScope.launch { localEventContainer.emit(this@emit) }

    fun <T, R> StateFlow<T>.mapAsState(
        started: SharingStarted = SharingStarted.Eagerly,
        transform: (T) -> R,
    ) = map(transform).stateIn(viewModelScope, started, transform(value))

    fun collectLocalEvents(function: suspend (LocalEvent) -> Unit) =
        viewModelScope.launch { localEvent.collect { function(it) } }
}
