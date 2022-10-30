package io.chaldeaprjkt.yumetsuki.data.dataswitch.source

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.flowAsResult
import io.chaldeaprjkt.yumetsuki.data.dataswitch.DataSwitchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSwitchNetworkSourceImpl @Inject constructor(
    private val dataSwitchApi: DataSwitchApi
) : DataSwitchNetworkSource {
    override suspend fun get(
        gameId: Int,
        switchId: Int,
        isPublic: Boolean,
        cookie: String,
        ds: String,
    ) = dataSwitchApi.changeDataSwitch(gameId, switchId, isPublic, cookie, ds)
        .flowAsResult(HoYoEmptyData())
        .flowOn(Dispatchers.IO)
}
