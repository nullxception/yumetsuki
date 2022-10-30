package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckIn
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import kotlinx.coroutines.flow.Flow

interface CheckInRepo {
    suspend fun genshin(cookie: String): Flow<HoYoResult<CheckIn>>
    suspend fun houkai(cookie: String): Flow<HoYoResult<CheckIn>>
}
