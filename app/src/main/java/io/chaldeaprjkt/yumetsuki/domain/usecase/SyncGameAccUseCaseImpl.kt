package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCard
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SyncGameAccUseCaseImpl @Inject constructor(
    private val gameAccountRepo: GameAccountRepo,
    private val userRepo: UserRepo,
) : SyncGameAccUseCase {

    private suspend fun storeAndActivate(hoyolabUid: Int, result: List<RecordCard>) {
        if (result.isEmpty()) return

        val accs = result.map { GameAccount.fromNetworkSource(hoyolabUid, it) }
        gameAccountRepo.store(accs)

        // Opportunistically activate the account
        listOf(HoYoGame.Genshin, HoYoGame.Houkai, HoYoGame.StarRail).forEach { game ->
            if (gameAccountRepo.getActive(game).firstOrNull() == null) {
                accs.firstOrNull { it.game == game }?.let {
                    gameAccountRepo.update(it.copy(active = true))
                }
            }
        }
    }

    override suspend operator fun invoke(user: User) =
        gameAccountRepo.fetch(user.cookie).onEach { res ->
            if (res is HoYoData) {
                userRepo.update(user.copy(gameAccountsSyncTimestamp = System.currentTimeMillis()))
                storeAndActivate(user.uid, res.data.list)
            }
        }
}
