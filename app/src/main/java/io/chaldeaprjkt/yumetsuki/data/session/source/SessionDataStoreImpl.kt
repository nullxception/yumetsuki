package io.chaldeaprjkt.yumetsuki.data.session.source

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.store.yumeDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDataStoreImpl
@Inject
constructor(
    moshi: Moshi,
    @ApplicationContext private val context: Context,
) : SessionDataStore {

    private val Context.dataStore by yumeDataStore(moshi, Session.key, Session.Empty)

    override val data = context.dataStore.data

    override suspend fun update(transform: suspend (Session) -> Session) =
        context.dataStore.updateData(transform)

    override suspend fun clear() {
        update { Session.Empty }
    }
}
