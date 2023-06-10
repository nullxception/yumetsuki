package io.chaldeaprjkt.yumetsuki.data.dataswitch.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import kotlinx.coroutines.flow.Flow

interface DataSwitchNetworkSource {
    suspend fun get(
        gameId: Int,
        switchId: Int,
        isPublic: Boolean,
        cookie: String,
        ds: String = CommonFunction.genDS(),
    ): Flow<HoYoResult<HoYoEmptyData>>
}
