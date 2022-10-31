package io.chaldeaprjkt.yumetsuki.data.checkin.source

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import kotlinx.coroutines.flow.Flow

interface CheckInNetworkSource {
    suspend fun status(
        cookie: String,
        game: HoYoGame
    ): Flow<HoYoResult<CheckInNoteResult>>

    suspend fun checkIn(
        cookie: String,
        game: HoYoGame
    ): Flow<HoYoResult<CheckInResult>>
}
