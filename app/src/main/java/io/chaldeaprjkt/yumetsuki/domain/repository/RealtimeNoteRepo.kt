package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import kotlinx.coroutines.flow.Flow

interface RealtimeNoteRepo {
    val data: Flow<RealtimeNote>
    suspend fun sync(uid: Int, server: GameServer, cookie: String): Flow<HoYoResult<RealtimeNote>>
}
