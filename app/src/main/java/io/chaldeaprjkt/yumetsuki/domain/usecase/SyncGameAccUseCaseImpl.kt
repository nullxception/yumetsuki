package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCard
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SyncGameAccUseCaseImpl @Inject constructor(
    private val gameAccountRepo: GameAccountRepo,
    private val userRepo: UserRepo,
    private val checkInRepo: CheckInRepo,
) : SyncGameAccUseCase {

    private suspend fun storeAndActivate(hoyolabUid: Int, result: List<RecordCard>) {
        if (result.isEmpty()) return

        val accs = result.map { GameAccount.fromNetworkSource(hoyolabUid, it) }
        userRepo.ofId(hoyolabUid).firstOrNull()?.let { gameAccountRepo.clear(it) }
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

    private suspend fun createStarRailStubCard(user: User): RecordCard {
        val acc = GameAccount.StarRailStub.copy(hoyolabUid = user.uid)
        val res = checkInRepo.sync(user, acc).firstOrNull()
        if (res is HoYoData<CheckInNoteResult>) {
            return RecordCard(acc.uid, acc.game, acc.level, acc.nickname, res.data.region)
        }

        return RecordCard.Empty
    }

    override suspend operator fun invoke(user: User) =
        gameAccountRepo.fetch(user.cookie).onEach { res ->
            if (res is HoYoData) {
                userRepo.update(user.copy(gameAccountsSyncTimestamp = System.currentTimeMillis()))
                val cards = res.data.list.toMutableList()
                val starRailCard = createStarRailStubCard(user)
                if (starRailCard.region.isNotEmpty()) {
                    cards.add(starRailCard)
                }
                storeAndActivate(user.uid, cards)
            }
        }
}
