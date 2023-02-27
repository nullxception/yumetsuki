package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import kotlinx.coroutines.flow.Flow

interface SyncGameAccUseCase {
    suspend operator fun invoke(user: User): Flow<HoYoResult<RecordCardApiResult>>
}
