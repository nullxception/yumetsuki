package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import kotlinx.coroutines.flow.Flow

interface CheckInRepo {
    suspend fun sync(user: User, gameAccount: GameAccount): Flow<HoYoResult<CheckInNoteResult>>
    suspend fun checkIn(user: User, gameAccount: GameAccount): Flow<HoYoResult<CheckInResult>>
    val data: Flow<List<CheckInNote>>
}
