package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCard
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountDao
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountNetworkSource
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.common.RepoResult
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAccountRepoImpl @Inject constructor(
    private val gameAccountNetworkSource: GameAccountNetworkSource,
    private val gameAccountDao: GameAccountDao,
    private val userRepo: UserRepo,
) : GameAccountRepo {

    override val accounts = gameAccountDao.all()
    override val actives = gameAccountDao.actives()
    override val activeGenshin = gameAccountDao.activeOn(HoYoGame.Genshin)
    override val activeHoukai = gameAccountDao.activeOn(HoYoGame.Houkai)

    override suspend fun fetch(cookie: String) = gameAccountNetworkSource.fetch(cookie)

    override suspend fun store(accounts: List<GameAccount>) {
        gameAccountDao.insert(*accounts.toTypedArray())
    }

    override suspend fun clear(user: User) {
        gameAccountDao.deleteBy(user.uid)
    }

    override suspend fun update(account: GameAccount) {
        gameAccountDao.update(account)
    }

    private suspend fun tryActivateGameAccounts(accounts: List<GameAccount>) {
        if (activeGenshin.firstOrNull() == null) {
            accounts.firstOrNull { it.game == HoYoGame.Genshin }?.let {
                update(it.copy(active = true))
            }
        }
        if (activeHoukai.firstOrNull() == null) {
            accounts.firstOrNull { it.game == HoYoGame.Houkai }?.let {
                update(it.copy(active = true))
            }
        }
    }

    private suspend fun storeGameAccount(hoyoUID: Int, result: List<RecordCard>) {
        val accounts = result.map { GameAccount.fromNetworkSource(hoyoUID, it) }
        store(accounts)
        tryActivateGameAccounts(accounts)
    }

    override suspend fun syncGameAccount(user: User) = flow {
        emit(RepoResult.Loading(R.string.fetching_in_game_data))
        fetch(user.cookie).collect { res ->
            if (res is HoYoResult.Success && res.code == HoYoApiCode.Success) {
                userRepo.update(user.copy(gameAccountsSyncTimestamp = System.currentTimeMillis()))
                val cards = res.data
                if (cards.list.isNotEmpty()) {
                    storeGameAccount(user.uid, cards.list)
                }
                emit(RepoResult.Success(R.string.success_fetching_ingame_info))
            } else {
                emit(RepoResult.Error(R.string.fail_get_ingame_data))
            }
        }
    }
}
