package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
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

    override suspend operator fun invoke(game: HoYoGame) = flow {
        val active = gameAccountRepo.getActive(game).firstOrNull() ?: return@flow
        val user = userRepo.ofId(active.hoyolabUid).firstOrNull() ?: return@flow
        checkInRepo.sync(user, active).collect(this)
    }
}
