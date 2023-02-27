package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepo {
    val data: Flow<Session>
    suspend fun update(transform: suspend (Session) -> Session): Session
    suspend fun clear()
}
