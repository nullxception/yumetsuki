package io.chaldeaprjkt.yumetsuki.data.realtimenote

import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteDataStore
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteNetworkSource
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeNoteRepoImpl @Inject constructor(
    private val realtimeNoteNetworkSource: RealtimeNoteNetworkSource,
    private val realtimeNoteDataStore: RealtimeNoteDataStore,
) : RealtimeNoteRepo {

    override val data get() = realtimeNoteDataStore.data

    override suspend fun sync(uid: Int, server: GameServer, cookie: String) =
        realtimeNoteNetworkSource.fetch(uid = uid, server = server, cookie = cookie)
            .onEach { res ->
                if (res is HoYoData) {
                    realtimeNoteDataStore.update { res.data }
                }
            }
}
