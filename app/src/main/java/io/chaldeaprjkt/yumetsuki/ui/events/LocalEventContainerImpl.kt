package io.chaldeaprjkt.yumetsuki.ui.events

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class LocalEventContainerImpl @Inject constructor() : LocalEventContainer {
    private val _event = MutableSharedFlow<LocalEvent>()
    override val event = _event.asSharedFlow()

    override suspend fun emit(newEvent: LocalEvent) = _event.emit(newEvent)
}
