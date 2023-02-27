package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import kotlinx.coroutines.flow.Flow

interface DataSwitchRepo {
    suspend fun get(
        gameId: Int,
        switchId: Int,
        isPublic: Boolean,
        cookie: String,
    ): Flow<HoYoResult<HoYoEmptyData>>
}
