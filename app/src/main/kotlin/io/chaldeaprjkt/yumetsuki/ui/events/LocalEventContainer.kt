package io.chaldeaprjkt.yumetsuki.ui.events

import kotlinx.coroutines.flow.SharedFlow

interface LocalEventContainer {
    val event: SharedFlow<LocalEvent>
    suspend fun emit(newEvent: LocalEvent)
}
