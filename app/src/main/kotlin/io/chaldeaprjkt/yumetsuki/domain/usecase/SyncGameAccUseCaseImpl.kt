package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCard
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.common.UseCaseResult
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SyncGameAccUseCaseImpl @Inject constructor(
    private val gameAccountRepo: GameAccountRepo,
    private val userRepo: UserRepo,
) : SyncGameAccUseCase {

    private suspend fun storeGameAccount(hoyolabUid: Int, result: List<RecordCard>) {
        if (result.isEmpty()) return

        val accs = result.map { GameAccount.fromNetworkSource(hoyolabUid, it) }
        gameAccountRepo.store(accs)

        // Opportunistically activate the account
        if (gameAccountRepo.activeGenshin.firstOrNull() == null) {
            accs.firstOrNull { it.game == HoYoGame.Genshin }?.let {
                gameAccountRepo.update(it.copy(active = true))
            }
        }
        if (gameAccountRepo.activeHoukai.firstOrNull() == null) {
            accs.firstOrNull { it.game == HoYoGame.Houkai }?.let {
                gameAccountRepo.update(it.copy(active = true))
            }
        }
    }

    override suspend operator fun invoke(user: User) = flow {
        emit(UseCaseResult.Loading(R.string.fetching_in_game_data))
        gameAccountRepo.fetch(user.cookie).collect { res ->
            if (res is HoYoData) {
                userRepo.update(user.copy(gameAccountsSyncTimestamp = System.currentTimeMillis()))
                storeGameAccount(user.uid, res.data.list)
                emit(UseCaseResult.Success(R.string.success_fetching_ingame_info))
            } else {
                emit(UseCaseResult.Error(R.string.fail_get_ingame_data))
            }
        }
    }
}
