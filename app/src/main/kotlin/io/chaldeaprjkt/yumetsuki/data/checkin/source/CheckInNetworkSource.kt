package io.chaldeaprjkt.yumetsuki.data.checkin.source

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckIn
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import kotlinx.coroutines.flow.Flow

interface CheckInNetworkSource {
    suspend fun genshin(
        lang: String,
        actId: String,
        cookie: String,
    ): Flow<HoYoResult<CheckIn>>

    suspend fun houkai(
        lang: String,
        actId: String,
        cookie: String,
    ): Flow<HoYoResult<CheckIn>>
}
