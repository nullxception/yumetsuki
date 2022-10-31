package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.common.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface SyncGameAccUseCase {
    suspend operator fun invoke(user: User): Flow<UseCaseResult>
}
