package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.common.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface SyncCheckInStatusUseCase {
    suspend operator fun invoke(game: HoYoGame): Flow<UseCaseResult>
}
