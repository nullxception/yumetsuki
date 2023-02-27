package io.chaldeaprjkt.yumetsuki.data.session

import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.session.source.SessionDataStore
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepoImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore
) : SessionRepo {

    override val data = sessionDataStore.data

    override suspend fun update(transform: suspend (Session) -> Session) =
        sessionDataStore.update(transform)

    override suspend fun clear() {
        update { Session.Empty }
    }
}
