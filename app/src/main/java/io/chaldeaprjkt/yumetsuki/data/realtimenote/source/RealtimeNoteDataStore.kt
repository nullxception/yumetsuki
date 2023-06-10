package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteDataStore {
    val dataGenshin: Flow<GenshinRealtimeNote>
    val dataStarRail: Flow<StarRailRealtimeNote>
    suspend fun updateGenshin(
        transform: suspend (GenshinRealtimeNote) -> GenshinRealtimeNote
    ): GenshinRealtimeNote
    suspend fun updateStarRail(
        transform: suspend (StarRailRealtimeNote) -> StarRailRealtimeNote
    ): StarRailRealtimeNote
}
