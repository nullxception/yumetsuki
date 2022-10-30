package io.chaldeaprjkt.yumetsuki.data.gameaccount.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import kotlinx.coroutines.flow.Flow

interface GameAccountNetworkSource {
    suspend fun fetch(cookie: String): Flow<HoYoResult<RecordCardApiResult>>
}
