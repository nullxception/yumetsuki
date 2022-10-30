package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserFullInfo
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    val users: Flow<List<User>>
    suspend fun fetch(cookie: String): Flow<HoYoResult<UserFullInfo>>
    suspend fun add(user: User)
    suspend fun update(user: User)
    suspend fun clear(user: User)
    suspend fun ofId(uid: Int): Flow<User?>
    suspend fun ownerOfGameAccount(acc: GameAccount): Flow<User?>
    suspend fun activeUserOf(game: HoYoGame): Flow<User?>
}
