package io.chaldeaprjkt.yumetsuki.domain.usecase

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoError
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.util.elog
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestCheckInUseCaseImpl @Inject constructor(
    private val checkInRepo: CheckInRepo,
    private val userRepo: UserRepo,
    private val gameAccountRepo: GameAccountRepo,
) : RequestCheckInUseCase {

    private suspend fun activeGameAccount(game: HoYoGame) =
        gameAccountRepo.run { if (game == HoYoGame.Genshin) activeGenshin else activeHoukai }
            .firstOrNull()

    override suspend operator fun invoke(game: HoYoGame) = flow {
        val accErr = HoYoError.Api<CheckInResult>(HoYoApiCode.AccountNotFound)
        val active = activeGameAccount(game) ?: let {
            emit(accErr)
            return@flow
        }

        checkInRepo.data.firstOrNull()?.let {
            if (it.any { x -> x.game == game && x.checkedToday() }) {
                elog("${game.name} checked today!")
                emit(HoYoError.Api(HoYoApiCode.CheckedIntoHoyolab))
                return@flow
            }
        }

        userRepo.ownerOfGameAccount(active).firstOrNull()?.let {
            checkInRepo.checkIn(it, active).collect(this)
        } ?: emit(accErr)
    }
}
