package io.chaldeaprjkt.yumetsuki.data.gameaccount.source

import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.GameAccountApi
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAccountNetworkSourceImpl @Inject constructor(
    private val gameAccountsApi: GameAccountApi
) : GameAccountNetworkSource {
    override suspend fun fetch(cookie: String) = flow<HoYoResult<RecordCardApiResult>> {
        val hoyolabUid = HoYoCookie(cookie).uid
        if (hoyolabUid == 0) {
            emit(HoYoResult.Null())
            return@flow
        }

        gameAccountsApi.get(hoyolabUid, cookie, CommonFunction.getGenshinDS())
            .suspendOnSuccess {
                emit(response.body()?.let {
                    HoYoResult.Success(
                        it.retcode,
                        it.message,
                        it.data ?: RecordCardApiResult.Empty
                    )
                } ?: HoYoResult.Null())
            }.suspendOnError {
                emit(HoYoResult.Failure(response.code(), response.message()))
            }.suspendOnException {
                emit(HoYoResult.Error(exception))
            }
    }.flowOn(Dispatchers.IO)
}
