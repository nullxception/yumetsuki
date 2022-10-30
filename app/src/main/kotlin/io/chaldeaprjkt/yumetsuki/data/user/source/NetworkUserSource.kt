package io.chaldeaprjkt.yumetsuki.data.user.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserFullInfo
import kotlinx.coroutines.flow.Flow

interface NetworkUserSource {
    suspend fun fetch(cookie: String): Flow<HoYoResult<UserFullInfo>>
}
