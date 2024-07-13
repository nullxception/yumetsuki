package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ZZZRealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteNetworkSource {
    suspend fun fetchGenshin(
        uid: Int,
        server: GameServer,
        cookie: String,
    ): Flow<HoYoResult<GenshinRealtimeNote>>

    suspend fun fetchStarRail(
        uid: Int,
        server: GameServer,
        cookie: String,
    ): Flow<HoYoResult<StarRailRealtimeNote>>

    suspend fun fetchZZZ(
        uid: Int,
        server: GameServer,
        cookie: String,
    ): Flow<HoYoResult<ZZZRealtimeNote>>
}
