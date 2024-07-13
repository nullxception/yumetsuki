package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.RealtimeNoteApi
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ZZZRealtimeNote
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

@Singleton
class RealtimeNoteNetworkSourceImpl
@Inject
constructor(
    private val realtimeNoteApi: RealtimeNoteApi,
) : RealtimeNoteNetworkSource {
    override suspend fun fetchGenshin(
        uid: Int,
        server: GameServer,
        cookie: String,
    ) =
        realtimeNoteApi
            .genshinNote(uid, server.regionId, cookie, CommonFunction.genDS())
            .flowAsResult(GenshinRealtimeNote.Empty)
            .flowOn(Dispatchers.IO)

    override suspend fun fetchStarRail(
        uid: Int,
        server: GameServer,
        cookie: String,
    ) =
        realtimeNoteApi
            .starRailNote(uid, server.regionId, cookie, CommonFunction.genDS())
            .flowAsResult(StarRailRealtimeNote.Empty)
            .flowOn(Dispatchers.IO)

    override suspend fun fetchZZZ(
        uid: Int,
        server: GameServer,
        cookie: String,
    ) =
        realtimeNoteApi
            .zzzNote(uid, server.regionId, cookie, CommonFunction.genDS())
            .flowAsResult(ZZZRealtimeNote.Empty)
            .flowOn(Dispatchers.IO)
}
