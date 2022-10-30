package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.common.RepoResult
import kotlinx.coroutines.flow.Flow

interface GameAccountRepo {
    val accounts: Flow<List<GameAccount>>
    val actives: Flow<List<GameAccount>>
    val activeHoukai: Flow<GameAccount?>
    val activeGenshin: Flow<GameAccount?>

    suspend fun fetch(cookie: String): Flow<HoYoResult<RecordCardApiResult>>
    suspend fun store(accounts: List<GameAccount>)
    suspend fun clear(user: User)
    suspend fun update(account: GameAccount)
    suspend fun syncGameAccount(user: User): Flow<RepoResult>
}
