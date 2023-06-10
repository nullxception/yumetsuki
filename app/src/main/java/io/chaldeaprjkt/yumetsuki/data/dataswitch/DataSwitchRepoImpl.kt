package io.chaldeaprjkt.yumetsuki.data.dataswitch

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.dataswitch.source.DataSwitchNetworkSource
import io.chaldeaprjkt.yumetsuki.domain.repository.DataSwitchRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class DataSwitchRepoImpl
@Inject
constructor(private val dataSwitchNetworkSource: DataSwitchNetworkSource) : DataSwitchRepo {
    override suspend fun get(
        gameId: Int,
        switchId: Int,
        isPublic: Boolean,
        cookie: String,
    ): Flow<HoYoResult<HoYoEmptyData>> =
        dataSwitchNetworkSource.get(
            gameId = gameId,
            switchId = switchId,
            isPublic = isPublic,
            cookie = cookie
        )
}
