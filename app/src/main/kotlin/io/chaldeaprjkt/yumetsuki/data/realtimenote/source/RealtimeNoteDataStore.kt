package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteDataStore {
    val data: Flow<RealtimeNote>
    suspend fun update(transform: suspend (RealtimeNote) -> RealtimeNote): RealtimeNote
}
