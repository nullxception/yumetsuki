package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteNetworkSource {
    suspend fun fetch(
        uid: Int,
        server: GameServer,
        cookie: String,
    ): Flow<HoYoResult<RealtimeNote>>
}
