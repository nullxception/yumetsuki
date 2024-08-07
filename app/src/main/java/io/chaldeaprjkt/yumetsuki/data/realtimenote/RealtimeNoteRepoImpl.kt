package io.chaldeaprjkt.yumetsuki.data.realtimenote

import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteDataStore
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteNetworkSource
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.onEach

@Singleton
class RealtimeNoteRepoImpl
@Inject
constructor(
    private val realtimeNoteNetworkSource: RealtimeNoteNetworkSource,
    private val realtimeNoteDataStore: RealtimeNoteDataStore,
) : RealtimeNoteRepo {

    override val dataGenshin
        get() = realtimeNoteDataStore.dataGenshin
    override val dataStarRail
        get() = realtimeNoteDataStore.dataStarRail
    override val dataZZZ
        get() = realtimeNoteDataStore.dataZZZ

    override suspend fun syncGenshin(uid: Int, server: GameServer, cookie: String) =
        realtimeNoteNetworkSource
            .fetchGenshin(uid = uid, server = server, cookie = cookie)
            .onEach { res ->
                if (res is HoYoData) {
                    realtimeNoteDataStore.updateGenshin { res.data }
                }
            }

    override suspend fun syncStarRail(uid: Int, server: GameServer, cookie: String) =
        realtimeNoteNetworkSource
            .fetchStarRail(uid = uid, server = server, cookie = cookie)
            .onEach { res ->
                if (res is HoYoData) {
                    realtimeNoteDataStore.updateStarRail { res.data }
                }
            }


    override suspend fun syncZZZ(uid: Int, server: GameServer, cookie: String) =
        realtimeNoteNetworkSource
            .fetchZZZ(uid = uid, server = server, cookie = cookie)
            .onEach { res ->
                if (res is HoYoData) {
                    realtimeNoteDataStore.updateZZZ { res.data }
                }
            }
}
