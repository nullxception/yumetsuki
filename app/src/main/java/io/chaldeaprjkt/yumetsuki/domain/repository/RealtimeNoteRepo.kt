package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ZZZRealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteRepo {
    val dataGenshin: Flow<GenshinRealtimeNote>
    val dataStarRail: Flow<StarRailRealtimeNote>
    val dataZZZ: Flow<ZZZRealtimeNote>
    suspend fun syncGenshin(
        uid: Int,
        server: GameServer,
        cookie: String
    ): Flow<HoYoResult<GenshinRealtimeNote>>

    suspend fun syncStarRail(
        uid: Int,
        server: GameServer,
        cookie: String
    ): Flow<HoYoResult<StarRailRealtimeNote>>

    suspend fun syncZZZ(
        uid: Int,
        server: GameServer,
        cookie: String
    ): Flow<HoYoResult<ZZZRealtimeNote>>
}
