package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.common.UseCaseResult
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SyncCheckInStatusUseCaseImpl @Inject constructor(
    private val checkInRepo: CheckInRepo,
    private val userRepo: UserRepo,
    private val gameAccountRepo: GameAccountRepo,
) : SyncCheckInStatusUseCase {

    private suspend fun activeGameAccount(game: HoYoGame) =
        gameAccountRepo.run { if (game == HoYoGame.Genshin) activeGenshin else activeHoukai }
            .firstOrNull()

    override suspend operator fun invoke(game: HoYoGame) = flow {
        val active = activeGameAccount(game) ?: return@flow
        val user = userRepo.ownerOfGameAccount(active).firstOrNull() ?: return@flow
        emit(UseCaseResult.Loading(R.string.sync_check_in_status))
        checkInRepo.sync(user, active).collect { res ->
            if (res is HoYoData) {
                emit(UseCaseResult.Success(R.string.success_fetching_ingame_info))
            } else {
                emit(UseCaseResult.Error(R.string.fail_get_ingame_data))
            }
        }
    }
}
