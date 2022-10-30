package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInStatus
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import kotlinx.coroutines.flow.Flow

interface CheckInRepo {
    suspend fun checkIn(cookie: String, game: HoYoGame): Flow<HoYoResult<CheckInResult>>
    suspend fun status(cookie: String, game: HoYoGame): Flow<HoYoResult<CheckInStatus>>
}
