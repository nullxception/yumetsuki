package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import kotlinx.coroutines.flow.Flow

interface SyncCheckInStatusUseCase {
    suspend operator fun invoke(game: HoYoGame): Flow<HoYoResult<CheckInNoteResult>>
}
