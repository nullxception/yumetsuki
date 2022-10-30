package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.data.store.yumeDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeNoteDataStoreImpl @Inject constructor(
    moshi: Moshi,
    @ApplicationContext private val context: Context,
) : RealtimeNoteDataStore {

    private val Context.dataStore by yumeDataStore(moshi, RealtimeNote.key, RealtimeNote.Empty)

    override val data = context.dataStore.data

    override suspend fun update(transform: suspend (RealtimeNote) -> RealtimeNote) =
        context.dataStore.updateData(transform)
}
