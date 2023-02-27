package io.chaldeaprjkt.yumetsuki.data.session.source

import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import kotlinx.coroutines.flow.Flow

interface SessionDataStore {
    val data: Flow<Session>
    suspend fun update(transform: suspend (Session) -> Session): Session
    suspend fun clear()
}
