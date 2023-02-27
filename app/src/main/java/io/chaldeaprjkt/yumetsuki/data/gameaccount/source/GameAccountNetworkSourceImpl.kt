package io.chaldeaprjkt.yumetsuki.data.gameaccount.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoError
import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
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
    override suspend fun fetch(cookie: String) = flow {
        val hoyolabUid = HoYoCookie(cookie).uid
        if (hoyolabUid == 0) {
            emit(HoYoError.Empty())
            return@flow
        }

        gameAccountsApi.get(hoyolabUid, cookie, CommonFunction.getGenshinDS())
            .flowAsResult(RecordCardApiResult.Empty).flowOn(Dispatchers.IO)
            .collect(this)
    }
}
