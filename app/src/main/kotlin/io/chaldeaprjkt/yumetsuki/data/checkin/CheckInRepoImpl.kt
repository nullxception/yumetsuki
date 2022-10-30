package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSource
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInRepoImpl @Inject constructor(
    private val checkInNetworkSource: CheckInNetworkSource,
) : CheckInRepo {

    override suspend fun status(cookie: String, game: HoYoGame) =
        checkInNetworkSource.status(
            cookie = cookie,
            game = game,
        )

    override suspend fun checkIn(cookie: String, game: HoYoGame): Flow<HoYoResult<CheckInResult>> =
        checkInNetworkSource.checkIn(
            cookie = cookie,
            game = game,
        )
}
