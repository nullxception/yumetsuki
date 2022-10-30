package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckIn
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSource
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInRepoImpl @Inject constructor(
    private val checkInNetworkSource: CheckInNetworkSource,
) : CheckInRepo {
    override suspend fun genshin(cookie: String): Flow<HoYoResult<CheckIn>> =
        checkInNetworkSource.genshin(
            lang = HoYoCookie(cookie).lang,
            actId = Source.Genshin.ActID,
            cookie = cookie,
        )

    override suspend fun houkai(cookie: String): Flow<HoYoResult<CheckIn>> =
        checkInNetworkSource.houkai(
            lang = HoYoCookie(cookie).lang,
            actId = Source.Houkai.ActID,
            cookie = cookie,
        )
}
