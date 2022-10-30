package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.RealtimeNoteApi
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeNoteNetworkSourceImpl @Inject constructor(
    private val realtimeNoteApi: RealtimeNoteApi,
) : RealtimeNoteNetworkSource {
    override suspend fun fetch(
        uid: Int,
        server: GameServer,
        cookie: String,
    ) = realtimeNoteApi.dailyNote(uid, server.regionId, cookie, CommonFunction.getGenshinDS())
        .flowAsResult(RealtimeNote.Empty).flowOn(Dispatchers.IO)
}
