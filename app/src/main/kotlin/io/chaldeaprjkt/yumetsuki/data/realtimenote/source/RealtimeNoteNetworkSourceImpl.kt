package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameServer
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.realtimenote.RealtimeNoteApi
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
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
    ) = flow<HoYoResult<RealtimeNote>> {
        realtimeNoteApi.dailyNote(uid, server.regionId, cookie, CommonFunction.getGenshinDS())
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(it.retcode, it.message, it.data ?: RealtimeNote.Empty)
                } ?: HoYoResult.Null())
            }
            .suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }
            .suspendOnException {
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)
}
