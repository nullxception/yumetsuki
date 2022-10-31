package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountDao
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountNetworkSource
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAccountRepoImpl @Inject constructor(
    private val gameAccountNetworkSource: GameAccountNetworkSource,
    private val gameAccountDao: GameAccountDao,
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
}
